package icu.axospark.service.iml;

import icu.axospark.mapper.*;
import icu.axospark.mq.producer.VideoTranscodeProducer;
import icu.axospark.pojo.dto.VideosDTO;
import icu.axospark.pojo.entity.Users;
import icu.axospark.pojo.entity.VideoTagRelations;
import icu.axospark.pojo.entity.VideoTags;
import icu.axospark.pojo.entity.Videos;
import icu.axospark.pojo.message.VideoMessage;
import icu.axospark.pojo.vo.VideosVO;
import icu.axospark.properties.MinIOProperties;
import icu.axospark.service.VideoUploadService;
import icu.axospark.utils.fileutil.IOUtil;
import icu.axospark.utils.generator.VideoIDGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    @Autowired
    private VideoTagsMapper videoTagsMapper;
    @Autowired
    private VideoTagRelationsMapper videoTagRelationsMapper;
    @Autowired
    private VideoTranscodeProducer videoTranscodeProducer;
    /**
     * 视频上传
     * @param videoFile 视频上传文件信息
     * @param userId 用户ID
     */
    @Override
    @Transactional
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
        videos.setVideoId(videoId);
        videos.setFilePath(object);
        videos.setUserId(userId);
        videos.setOriginalName(originalFilename);
        videos.setFileSize(videoFile.getSize());
        videos.setStatus(0);

        InputStream inputStream = null;
        try {
            inputStream = videoFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentType = videoFile.getContentType();
        ioUtil.uploadStream(minioProperties.getBucket().getVideo(),object,inputStream,contentType);
        ZonedDateTime zonedDateTime = ioUtil.fileInfo(minioProperties.getBucket().getVideo(), object).lastModified();
        videos.setCreateTime(zonedDateTime.toLocalDateTime());
        videosMapper.insert(videos);
        VideoMessage videoMessage = new VideoMessage();
        videoMessage.setVideoId(videoId);
        videoTranscodeProducer.sendTranscodeTask(videoMessage);
        VideosVO videosVO = new VideosVO();
        videosVO.setId(videoId);
        videosVO.setCreateTime(zonedDateTime.toLocalDateTime());
        videosVO.setFilePath(object);
        videosVO.setCreateTime(zonedDateTime.toLocalDateTime());
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

        //保存基本信息以及categoryId
        BeanUtils.copyProperties(videosDTO,videos);
        //设置用户id
        videos.setUserId(userId);
        //设置信息状态
        videos.setStatus(1);

        videosMapper.updateByPrimaryKey(videos);
        //开始处理标签
        //处理前先删除，然后再新增
        videoTagRelationsMapper.deleteByVideoId(videoId);
        for (String tagName : videosDTO.getTagsNames()) {
            //1.检查是否在video_tags表中已存在该tag
            //  存在无需插入
            VideoTags videoTag = videoTagsMapper.findTagByName(tagName);
            //2.不存在
            //  可以插入
            if(videoTag==null){
                VideoTags newVideoTag = new VideoTags();
                newVideoTag.setTagName(tagName);
                videoTagsMapper.insert(newVideoTag);
                videoTag  = newVideoTag;
            }
            //处理视频与标签的关系表
            VideoTagRelations videoTagRelations = new VideoTagRelations();
            videoTagRelations.setTagId(videoTag.getTagId());
            videoTagRelations.setVideoId(videoId);
            videoTagRelationsMapper.insert(videoTagRelations);
        }

    }
}
