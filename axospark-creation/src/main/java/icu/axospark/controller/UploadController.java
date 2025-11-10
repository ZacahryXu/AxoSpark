package icu.axospark.controller;

import cn.dev33.satoken.stp.StpUtil;
import icu.axospark.entity.AxoSparkResult;
import icu.axospark.pojo.dto.ChunkDTO;
import icu.axospark.pojo.dto.CreateVideoDTO;
import icu.axospark.pojo.dto.PreUploadDTO;
import icu.axospark.pojo.vo.PreUploadVO;
import icu.axospark.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Tag(name="文件上传通用接口")
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;
    @Operation(summary = "预上传（判断当前的文件上传信息）")
    @PostMapping("/pre-upload")
    public AxoSparkResult<PreUploadVO> preUpload(@RequestBody  PreUploadDTO preUploadDTO){
        PreUploadVO preUploadVO=uploadService.preUpload(preUploadDTO, 1L);
        return AxoSparkResult.success(preUploadVO);
    }
    @Operation(summary = "获取每个分块URL")
    @PostMapping("/chunk-url")
    public AxoSparkResult<PreUploadVO> chunkUrl(@RequestBody PreUploadDTO preUploadDTO){
        PreUploadVO preUploadVO = uploadService.chunkUrl(preUploadDTO,1L);
        return AxoSparkResult.success(preUploadVO);
    }
    @Operation(summary = "chunk确认")
    @PostMapping("confirm-chunk")
    public AxoSparkResult<Void> confirmChunk(@RequestBody  ChunkDTO chunkDTO){
        uploadService.confirmChunk(chunkDTO);
        return AxoSparkResult.success();
    }

    @Operation(tags = "合并切片")
    @PostMapping("/merge")
    public AxoSparkResult<Void> merge(@RequestBody CreateVideoDTO createVideoDTO){
            uploadService.merge(createVideoDTO);
        return AxoSparkResult.success();
    }



}
