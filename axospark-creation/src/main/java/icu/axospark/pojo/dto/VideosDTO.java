package icu.axospark.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideosDTO {
    private Long id;
    private LocalDateTime createTime;
    private String title;
    private String description;
    private Long userId;
    private Long categoryId;
    private String filePath;
    private List<String> tagsNames;

}
