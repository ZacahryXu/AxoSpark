package icu.axospark.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "axospark.minio")
@Data
@Validated
public class MinIOProperties {
    /**
     * MinIO 服务地址
     * 示例: http://localhost:9000
     */
    @NotBlank(message = "MinIO endpoint 不能为空")
    private String endpoint;

    /**
     * 访问密钥 (Access Key)
     */
    @NotBlank(message = "MinIO accessKey 不能为空")
    private String accessKey;

    /**
     * 秘密密钥 (Secret Key)
     */
    @NotBlank(message = "MinIO secretKey 不能为空")
    private String secretKey;

    /**
     * 桶配置
     */
    private BucketConfig bucket = new BucketConfig();

    /**
     * 桶配置内部类
     */
    @Data
    public static class BucketConfig {
        /**
         * 视频桶名称
         */
        private String video = "videos";

        /**
         * 图片桶名称
         */
        private String image = "images";

        /**
         * 文档桶名称
         */
        private String document = "documents";

        /**
         * 音频桶名称
         */
        private String audio = "audios";
    }
}
