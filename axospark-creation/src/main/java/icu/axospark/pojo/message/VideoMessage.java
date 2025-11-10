package icu.axospark.pojo.message;

import lombok.Data;

@Data
public class VideoMessage {
    private Long videoId;
    private String videoName;
    private String publisher; // 视频发布者（用字符串代替）
    private String originalPath; // 原始视频路径
}
