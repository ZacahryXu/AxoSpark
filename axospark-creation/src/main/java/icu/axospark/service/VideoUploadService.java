package icu.axospark.service;

import icu.axospark.entity.AxoSparkResult;
import icu.axospark.pojo.dto.VideosDTO;
import icu.axospark.pojo.vo.VideosVO;
import org.springframework.web.multipart.MultipartFile;

public interface VideoUploadService {


    VideosVO uploadVideo(MultipartFile videoFile, Long userId);

    void postVideo(VideosDTO videosDTO,Long userId);
}
