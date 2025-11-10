package icu.axospark.pojo.entity;

import icu.axospark.entity.AxoSparkBaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户视频关联表
 * @TableName user_video_relation
 */
@Data
public class UserVideoRelation extends AxoSparkBaseEntity {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 关系类型: upload(上传)/collect(收藏)/like(点赞)
     */
    private String relationType;

    /**
     * 是否秒传: 0-正常上传, 1-秒传
     */
    private Integer isInstantUpload;

    /**
     * 秒传源视频ID
     */
    private Long sourceVideoId;

    /**
     * 是否所有者: 0-否, 1-是
     */
    private Integer isOwner;

    /**
     * 权限类型: view/edit/delete
     */
    private String permission;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date dAt;
}