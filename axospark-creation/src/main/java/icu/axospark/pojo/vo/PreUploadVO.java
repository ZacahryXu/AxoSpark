package icu.axospark.pojo.vo;

import lombok.Data;

import java.util.Set;

@Data
public class PreUploadVO {
    /*
    上传任务id
     */
    private String uploadId;
    private Long chunkSize;
    private Long chunkIndex;
    private Long totalChunks;
    private Long expire;
    private String presignedUrl;
    //哪些分块已上传
    private Set<String> uploadedChunks;
    //是否秒传
    private boolean isSecondTransfer;
    //上传完的视频路径
    private String preViewUrl;


}
