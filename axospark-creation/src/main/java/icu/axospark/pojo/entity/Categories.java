package icu.axospark.pojo.entity;

import java.util.Date;

import icu.axospark.entity.AxoSparkBaseEntity;
import lombok.Data;

/**
 * 视频分类表
 * @TableName categories
 */
@Data
public class Categories extends AxoSparkBaseEntity {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父分类ID(逻辑外键)
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态: 1-启用, 0-禁用
     */
    private Integer status;


}