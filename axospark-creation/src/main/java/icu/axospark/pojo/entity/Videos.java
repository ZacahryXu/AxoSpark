package icu.axospark.pojo.entity;

import icu.axospark.entity.AxoSparkBaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 视频表
 * @TableName videos
 */
@Data
public class Videos extends AxoSparkBaseEntity {
    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 上传用户ID(逻辑外键)
     */
    private Long userId;

    /**
     * 视频分类ID(逻辑外键)
     */
    private Long videoCategoryId;

    /**
     * 视频文件路径
     */
    private String filePath;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 视频时长(秒)
     */
    private Integer duration;

    /**
     * 视频宽度
     */
    private Integer width;

    /**
     * 视频高度
     */
    private Integer height;

    /**
     * 视频格式(mp4, avi等)
     */
    private String format;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 状态: 1-正常, 2-审核中, 3-审核失败, 0-删除
     */
    private Integer status;

    /**
     * 观看次数
     */
    private Long viewCount;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 分享数
     */
    private Long shareCount;

    /**
     * 是否公开: 1-公开, 0-私有
     */
    private Integer isPublic;


    /**
     * 原始文件名
     */
    private String originalName;
}