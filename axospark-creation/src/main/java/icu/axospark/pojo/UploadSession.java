package icu.axospark.pojo;

import lombok.Data;

@Data
public class UploadSession {
    private String uploadId;
    private String userId;
    private String fileHash;
    private String fileName;
    private Long fileSize;
    private Long totalChunks;
    private Long chunkSize;
    private String ObjectName;
}
