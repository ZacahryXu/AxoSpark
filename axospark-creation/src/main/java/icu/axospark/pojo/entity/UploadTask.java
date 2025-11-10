package icu.axospark.pojo.entity;

import icu.axospark.entity.AxoSparkBaseEntity;
import lombok.Data;

/**
 * 大文件上传任务日志表
 * @TableName upload_task
 */
@Data
public class UploadTask extends AxoSparkBaseEntity {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 上传会话ID(UUID)
     */
    private String uploadId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 视频ID(上传成功后关联)
     */
    private Long videoId;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 文件哈希值(MD5/SHA256)
     */
    private String fileHash;

    /**
     * 文件MIME类型
     */
    private String mimeType;

    /**
     * 分片大小(字节,默认5MB)
     */
    private Integer chunkSize;

    /**
     * 总分片数
     */
    private Long totalChunks;

    /**
     * 已上传分片数
     */
    private Integer uploadedChunks;

    /**
     * 临时存储路径
     */
    private String storagePath;

    /**
     * 合并后文件路径
     */
    private String mergedPath;

    /**
     * 状态: pending:0|hashing:1|uploading:2|paused:3|merging:4|completed:5|failed:6|expired:7
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String errorMsg;
    /**
     * 对象名
     */
    private String objectName;
}