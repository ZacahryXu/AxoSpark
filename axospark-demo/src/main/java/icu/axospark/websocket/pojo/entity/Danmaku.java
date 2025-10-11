package icu.axospark.websocket.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Danmaku {
    private Long id;
    private String content;
}
