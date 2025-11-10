package icu.axospark.pojo.dto;

import lombok.Data;

@Data
public class ChunkDTO {
    private String uploadId;
    private Integer chunkIndex;
    private String chunkHash;  // 用于验证
}
