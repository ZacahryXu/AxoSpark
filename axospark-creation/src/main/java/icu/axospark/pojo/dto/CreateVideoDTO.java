package icu.axospark.pojo.dto;

import lombok.Data;

@Data
public class CreateVideoDTO {
    private String fileHash;
    private String fileName;
    private Long fileSize;
    private String uploadId;
}
