package icu.axospark.pojo.entity;

import icu.axospark.entity.AxoSparkBaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 视频标签关联表
 * @TableName video_tag_relations
 */
@Data
public class VideoTagRelations extends AxoSparkBaseEntity {

    /**
     * 视频ID(逻辑外键)
     */
    private Long videoId;

    /**
     * 标签ID(逻辑外键)
     */
    private Long tagId;


}