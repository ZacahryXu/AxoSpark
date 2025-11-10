package icu.axospark.service.iml;

import com.alibaba.fastjson2.JSON;
import icu.axospark.mapper.LoginMapper;
import icu.axospark.mapper.UploadTaskMapper;
import icu.axospark.pojo.UploadSession;
import icu.axospark.pojo.dto.ChunkDTO;
import icu.axospark.pojo.dto.CreateVideoDTO;
import icu.axospark.pojo.dto.PreUploadDTO;
import icu.axospark.pojo.entity.UploadTask;
import icu.axospark.pojo.vo.PreUploadVO;
import icu.axospark.properties.MinIOProperties;
import icu.axospark.service.UploadService;
import icu.axospark.utils.fileutil.IOUtil;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IOUtil ioUtil;
    @Autowired
    private MinIOProperties minioProperties;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private UploadTaskMapper uploadTaskMapper;

    /**
     * 合并chunks
     * @param createVideoDTO 合并信息
     */
    @Override
    public void merge(CreateVideoDTO createVideoDTO)  {
        String fileHash = createVideoDTO.getFileHash();
        String fileName = createVideoDTO.getFileName();
        Long size = createVideoDTO.getFileSize();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        List<String> chunks = ioUtil.listChunks(minioProperties.getBucket().getTempChunk(), "temp/" + fileHash + "/");
        // 校验分片是否完整
        if (chunks.isEmpty()) {
            throw new RuntimeException("分片列表为空，无法合并");
        }
        // 排序

        chunks = chunks.stream()
                .sorted(Comparator.comparingInt(chunk ->
                        Integer.parseInt(chunk.substring(chunk.lastIndexOf("-") + 1)))).toList();
        // 合并分片
        long startTime = System.currentTimeMillis();
        String targetObject = "originalVideo/" + fileHash + ext;
        ObjectWriteResponse response = ioUtil.composeObject(
                minioProperties.getBucket().getTempChunk(),
                targetObject,
                chunks
        );
        long costTime = System.currentTimeMillis() - startTime;
        log.info("文件合并完成: fileHash={}, 耗时={}ms", fileHash, costTime);
        // 4. 更新上传任务状态（使用uploadId条件更新）
        UploadTask uploadTask = new UploadTask();
        uploadTask.setUploadId(createVideoDTO.getUploadId()); // 需要添加条件
        uploadTask.setStatus(1);
        uploadTask.setObjectName(targetObject); // 记录最终文件路径
        uploadTask.setUserId(1L);
        uploadTask.setFileName(fileName);
        uploadTask.setFileHash(fileHash);
        uploadTask.setFileSize(size);
        uploadTask.setMergedPath(targetObject);
        uploadTaskMapper.updateByUploadId(uploadTask); // 使用Selective避免覆盖其他字段

        //设置秒传缓存（添加过期时间）
        UploadSession uploadSession = new UploadSession();
        uploadSession.setFileHash(fileHash);
        uploadSession.setObjectName(targetObject);
        stringRedisTemplate.opsForValue().set(
                "file:exists:" + fileHash,
                JSON.toJSONString(uploadSession),
                30, TimeUnit.DAYS // 添加过期时间
        );
        //异步清理（避免阻塞主流程）
        CompletableFuture.runAsync(() -> {
            try {
                ioUtil.removeChunks(minioProperties.getBucket().getTempChunk(), "temp/" + fileHash + "/");
                stringRedisTemplate.delete("upload:chunks:" + createVideoDTO.getUploadId());
                log.info("临时分片清理完成: fileHash={}", fileHash);
            } catch (Exception e) {
                log.error("清理临时分片失败: fileHash={}", fileHash, e);
            }
        });

        //todo:异步通知生成默认封面

    }

    /**
     * 预上传（preUpload）就是前端与后端对这个大文件进行“建档”和“任务分配”的过程。
     * @param preUploadDTO 预上传信息
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional
    public PreUploadVO preUpload(PreUploadDTO preUploadDTO,Long userId) {
        //todo:身份校验

        String fileHash = preUploadDTO.getFileHash();
        Long fileSize = preUploadDTO.getFileSize();
        // 文件大小限制（例如：最大5GB）
        if (fileSize > 2L * 1024 * 1024 * 1024) {
            throw new RuntimeException("文件大小超过限制");
        }

        String fileName = preUploadDTO.getFileName();
        // 文件类型校验
        String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(".mp4", ".avi", ".mov", ".flv", ".mkv").contains(ext)) {
            throw new RuntimeException("不支持的文件格式");
        }
        Long totalChunks = preUploadDTO.getTotalChunks();
        //若文件已经上传了，返回上传信息即可
        String fileJson = stringRedisTemplate.opsForValue().get("file:exists:" + fileHash);
        UploadSession uploadSession = JSON.parseObject(fileJson, UploadSession.class);
        if(uploadSession!=null){
            //验证文件是否真实存在
            boolean exists = ioUtil.fileExists(
                    minioProperties.getBucket().getVideo(),
                    uploadSession.getObjectName()
            );
            if(!exists){
                //缓存失效，删除Redis
                stringRedisTemplate.delete("file:exists:"+fileHash);
            }else {
                String previewUrl = ioUtil.getPresignedUrl(minioProperties.getBucket().getVideo(), uploadSession.getObjectName());
                PreUploadVO preUploadVO = new PreUploadVO();
                preUploadVO.setUploadId(uploadSession.getUploadId());
                preUploadVO.setSecondTransfer(true);
                preUploadVO.setPreViewUrl(previewUrl);
                return preUploadVO;
            }
        }
        UploadTask task = new UploadTask();
        task.setUploadId(fileHash);
        task.setUserId(userId);

        //断点续传检查
        UploadTask existTask=uploadTaskMapper.selectNotUploaded(task);
        String uploadId;
        Set<String> uploadedChunks;
        //所有分块上传时都会保存在redis中，从redis查询已经上传的分块
        //[]就是文件第一次上传，[3,5,80-100]，就是chunkTotal总个数是100，目前3,5,80-100分块已上传
        if(existTask!=null){
            uploadId = existTask.getUploadId();
            uploadedChunks = stringRedisTemplate.opsForSet().members("upload:chunks:" + uploadId);
        }else{
            //若文件只是部分上传,返回成功上传的分块即可
            //若文件第一次上传，上传任务表写入基本信息
            // 3. 新上传任务：创建记录
            uploadId = getUploadId();

            UploadTask uploadTask = new UploadTask();
            uploadTask.setFileHash(fileHash);
            uploadTask.setFileSize(fileSize);
            uploadTask.setFileName(fileName);
            uploadTask.setUploadId(uploadId);
            uploadTask.setUserId(userId);
            uploadTask.setTotalChunks(totalChunks);
            uploadTask.setStatus(0);
            uploadTaskMapper.insert(uploadTask);

            uploadedChunks = Collections.emptySet();
        }
        // 返回上传信息
        PreUploadVO preUploadVO = new PreUploadVO();
        preUploadVO.setUploadId(uploadId);
        preUploadVO.setUploadedChunks(uploadedChunks);
        return preUploadVO;

    }

    /**
     * 每个分块处理
     * @param preUploadDTO 每个分块的信息
     * @return
     */
    @Override
    public PreUploadVO chunkUrl(PreUploadDTO preUploadDTO,Long userId) {
        String fileHash = preUploadDTO.getFileHash();
        String chunkHash = preUploadDTO.getChunkHash();
        String uploadId = preUploadDTO.getUploadId();
        UploadTask uploadTask = new UploadTask();
        uploadTask.setUploadId(uploadId);
        uploadTask.setFileHash(fileHash);

        UploadTask task=uploadTaskMapper.selectNotUploaded(uploadTask);
        if(task==null){
            throw new RuntimeException("上传任务不存在或已经完成");
        }
        //生成临时上传凭证
        String objectName = "temp/"+fileHash+"/"+chunkHash;
        String presignedUrl = ioUtil.getPresignedUrl(
                minioProperties.getBucket().getTempChunk(),
                objectName,
                3600);
        PreUploadVO preUploadVO = new PreUploadVO();
        preUploadVO.setChunkIndex(preUploadDTO.getChunkIndex());
        preUploadVO.setPresignedUrl(presignedUrl);
        preUploadVO.setExpire(3600L);
        return preUploadVO;
    }

    @Override
    public void confirmChunk(ChunkDTO chunkDTO) {
        String uploadId = chunkDTO.getUploadId();
        Integer chunkIndex = chunkDTO.getChunkIndex();
        // 记录已上传
        stringRedisTemplate.opsForSet().add("upload:chunks:" + uploadId, String.valueOf(chunkIndex));
        stringRedisTemplate.expire("upload:chunks:" + uploadId, 7, TimeUnit.DAYS);
    }

    private String getUploadId(){
        return UUID.randomUUID().toString();
    }
}
