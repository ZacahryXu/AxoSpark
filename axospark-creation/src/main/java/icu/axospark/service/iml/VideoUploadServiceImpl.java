package icu.axospark.service.iml;

import icu.axospark.entity.AxoSparkResult;
import icu.axospark.mapper.LoginMapper;
import icu.axospark.mapper.VideosMapper;
import icu.axospark.pojo.dto.VideosDTO;
import icu.axospark.pojo.entity.Users;
import icu.axospark.pojo.entity.Videos;
import icu.axospark.pojo.vo.VideosVO;
import icu.axospark.properties.MinIOProperties;
import icu.axospark.service.VideoUploadService;
import icu.axospark.utils.fileutil.IOUtil;
import icu.axospark.utils.generator.VideoIDGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class VideoUploadServiceImpl implements VideoUploadService {
    @Autowired
    private  IOUtil ioUtil;
    @Autowired
    private MinIOProperties minioProperties;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private VideosMapper videosMapper;

    /**
     * 视频上传
     * @param videoFile 视频上传文件信息
     * @param userId 用户ID
     */
    @Override
    public VideosVO uploadVideo(MultipartFile videoFile, Long userId) {
        Users users = loginMapper.selectByPrimaryKey(userId);
        String username = users.getUsername();
        LocalDate curDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        String date = curDate.format(formatter);
        String originalFilename = videoFile.getOriginalFilename();
        String extension = originalFilename.substring(videoFile.getOriginalFilename().lastIndexOf("."));
        VideoIDGenerator generator = new VideoIDGenerator();
        long videoId = generator.generate();
        //对象名称拼接
        String object =  date+username+"/"+ videoId+extension;
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setFilePath(object);
        videos.setUserId(userId);
        videos.setOriginalName(originalFilename);
        videos.setFileSize(videoFile.getSize());
        videos.setCreateTime(LocalDateTime.now());
        videos.setStatus(0);
        videosMapper.insert(videos);
        InputStream inputStream = null;
        try {
            inputStream = videoFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentType = videoFile.getContentType();
        ioUtil.uploadStream(minioProperties.getBucket().getVideo(),object,inputStream,contentType);
        VideosVO videosVO = new VideosVO();
        videosVO.setId(videoId);
        ZonedDateTime zonedDateTime = ioUtil.fileInfo(minioProperties.getBucket().getVideo(), object).lastModified();
        videosVO.setCreateTime(zonedDateTime.toLocalDateTime());
        String fileUrl = ioUtil.getFileUrl(minioProperties.getEndpoint(), minioProperties.getBucket().getVideo(), object);
        videosVO.setFilePath(fileUrl);
        return videosVO;

    }

    /**
     * 视频发布
     * @param videosDTO 视频发布信息实体类
     */
    @Override
    @Transactional
    public void postVideo(VideosDTO videosDTO,Long userId) {
        //根据视频id获取真正的url
        Long videoId = videosDTO.getId();
        Videos videos = videosMapper.selectByPrimaryKey(videoId);
        //todo:视频不存在，需要抛异常
        BeanUtils.copyProperties(videosDTO,videos);
        videos.setUserId(userId);
        //把视频信息保存到数据库
        videos.setStatus(1);
        videosMapper.updateByPrimaryKey(videos);

    }
}
