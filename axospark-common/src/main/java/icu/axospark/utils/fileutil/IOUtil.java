package icu.axospark.utils.fileutil;


import icu.axospark.properties.MinIOProperties;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class IOUtil {
    @Autowired
    private MinIOProperties minioProperties;

    private MinioClient minioClient;
    @PostConstruct
    public void init() {
        this.minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(),
                        minioProperties.getSecretKey())
                .build();
        log.info("✅ MinIO 客户端初始化成功 -> {}", minioProperties.getEndpoint());
    }



    /**
     * 上传文件
     *
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称（文件路径）
     * @param filePath   本地文件路径
     * @return 文件访问URL
     */
    public String uploadFile( String bucketName,
                                    String objectName, String filePath) {
        try {
            // 上传文件
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build()
            );
            log.info("文件上传成功: bucket={}, object={}, file={}", bucketName, objectName, filePath);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败: bucket={}, object={}, file={}", bucketName, objectName, filePath, e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传文件流
     *
     * @param bucketName  桶名称
     * @param objectName  对象名称
     * @param inputStream 文件流
     * @param contentType 文件类型
     * @return 对象名称
     */
    public String uploadStream(String bucketName,
                                      String objectName, InputStream inputStream,
                                      String contentType) {
        try {


            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );

            log.info("文件流上传成功: bucket={}, object={}", bucketName, objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件流上传失败: bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("文件流上传失败", e);
        }
    }

    /**
     * 上传字节数组
     *
     *
     * @param bucketName  桶名称
     * @param objectName  对象名称
     * @param bytes       字节数组
     * @param contentType 文件类型
     * @return 对象名称
     */
    public String uploadBytes( String bucketName,
                                     String objectName, byte[] bytes, String contentType) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return uploadStream(bucketName, objectName, bis, contentType);
        } catch (Exception e) {
            log.error("字节数组上传失败: bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("字节数组上传失败", e);
        }
    }

    /**
     * 下载文件
     *
     * @param client     MinIO 客户端
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return 文件流
     */
    public InputStream downloadFile(MinioClient client, String bucketName, String objectName) {
        try {
            return client.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("文件下载失败: bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     */
    public void deleteFile( String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("文件删除成功: bucket={}, object={}", bucketName, objectName);
        } catch (Exception e) {
            log.error("文件删除失败: bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("文件删除失败", e);
        }
    }

    /**
     * 获取文件访问URL（永久）
     *
     * @param endpoint   MinIO 服务地址
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return 文件URL
     */
    public String getFileUrl(String endpoint, String bucketName, String objectName) {
        return String.format("%s/%s/%s", endpoint, bucketName, objectName);
    }

    /**
     * 获取预签名URL（临时访问链接）
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param expires    过期时间（秒）
     * @return 预签名URL
     */
    public String getPresignedUrl(String bucketName,
                                         String objectName, int expires) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expires, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取预签名URL失败: bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("获取预签名URL失败", e);
        }
    }

    /**
     * 获取预签名URL（默认7天有效期）
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return 预签名URL
     */
    public String getPresignedUrl( String bucketName, String objectName) {
        return getPresignedUrl(bucketName, objectName, 7 * 24 * 3600);
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return true-存在，false-不存在
     */
    public boolean fileExists( String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public StatObjectResponse fileInfo(String bucketName, String objectName) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

        } catch (Exception e) {
            log.error("获取文件信息失败: bucket={}, object={}", bucketName, objectName, e);
            throw new RuntimeException("获取文件信息失败", e);
        }
    }
}
