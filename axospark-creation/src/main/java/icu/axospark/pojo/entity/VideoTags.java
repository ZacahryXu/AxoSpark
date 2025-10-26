package icu.axospark.pojo.entity;

import icu.axospark.entity.AxoSparkBaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 视频标签表
 * @TableName video_tags
 */
@Data
public class VideoTags extends AxoSparkBaseEntity {
    /**
     * 标签主键
     */
    private Long tagId;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 使用次数
     */
    private Long usageCount;

}