package icu.axospark.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AxoSparkBaseEntity implements Serializable {
    /** 主键ID */
    private Long id;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private String createBy;

    /** 更新人 */
    private String updateBy;

    /** 逻辑删除标记（0 = 正常, 1 = 已删除） */
    private Integer status = 0;

}
