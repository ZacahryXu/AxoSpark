package entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AxoSparkBaseEntity implements Serializable {

    /**
     * 主键ID（通常各子类定义具体类型，如 Long 或 String）
     */
    // private Long id; // 通常子类定义，避免不同表主键策略冲突

    /**
     * 创建时间（毫秒时间戳）
     */
    private Long createTime;

    /**
     * 更新时间（毫秒时间戳）
     */
    private Long updateTime;

    /**
     * 创建人（可选）
     */
    private String createBy;

    /**
     * 更新人（可选）
     */
    private String updateBy;

    /**
     * 是否删除（逻辑删除：0-未删，1-已删）
     */
    private Integer status = 0;

    /**
     * 插入前自动设置创建时间
     */
    public void preInsert() {
        long now = System.currentTimeMillis();
        this.createTime = now;
        this.updateTime = now;
    }

    /**
     * 更新前自动更新时间
     */
    public void preUpdate() {
        this.updateTime = System.currentTimeMillis();
    }
}
