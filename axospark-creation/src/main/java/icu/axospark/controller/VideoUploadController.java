package icu.axospark.controller;

import cn.dev33.satoken.stp.StpUtil;
import icu.axospark.entity.AxoSparkResult;
import icu.axospark.pojo.dto.PreUploadDTO;
import icu.axospark.pojo.dto.VideosDTO;
import icu.axospark.pojo.vo.VideosVO;
import icu.axospark.service.VideoUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@Tag(name = "视频上传请求")
@Slf4j
@RequestMapping("/video")
public class VideoUploadController {
    @Autowired
    private VideoUploadService videoUploadService;
    @PostMapping("/uploadVideo")
    @Operation(tags = "视频上传请求")
    public AxoSparkResult<VideosVO> uploadVideo(@RequestParam("videoFile") MultipartFile videoFile){

        VideosVO videosVO = videoUploadService.uploadVideo(videoFile, StpUtil.getLoginIdAsLong());
        return AxoSparkResult.success(videosVO);
    }
    @PostMapping("/postVideo")
    @Operation(tags = "视频发布请求")
    public AxoSparkResult  postVideo(@RequestBody VideosDTO videosDTO){
        videoUploadService.postVideo(videosDTO,StpUtil.getLoginIdAsLong());
        return AxoSparkResult.success();
    }


    /*String originalFilename = videoFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;
        try {
            videoFile.transferTo(new File("D:\\tempFiles\\videos\\"+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

}
