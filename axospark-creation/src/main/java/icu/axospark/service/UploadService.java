package icu.axospark.service;

import icu.axospark.pojo.dto.ChunkDTO;
import icu.axospark.pojo.dto.CreateVideoDTO;
import icu.axospark.pojo.dto.PreUploadDTO;
import icu.axospark.pojo.vo.PreUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {



    void merge(CreateVideoDTO createVideoDTO);


    PreUploadVO preUpload(PreUploadDTO preUploadDTO,Long userId);

    PreUploadVO chunkUrl(PreUploadDTO preUploadDTO,Long userId);

    void confirmChunk(ChunkDTO chunkDTO);
}
