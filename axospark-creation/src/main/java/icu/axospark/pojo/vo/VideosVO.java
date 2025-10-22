package icu.axospark.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideosVO {
    private Long id;
    private LocalDateTime createTime;
    private String filePath;
    private Integer duration;
}
