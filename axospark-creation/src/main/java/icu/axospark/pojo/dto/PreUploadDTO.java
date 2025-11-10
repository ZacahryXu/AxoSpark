package icu.axospark.pojo.dto;

import lombok.Data;

@Data
public class PreUploadDTO {
    private String fileHash;
    private String fileName;
    private Long fileSize;
    private Long chunkIndex;
    private String chunkHash;
    private Long totalChunks;
    private String uploadId;

}
