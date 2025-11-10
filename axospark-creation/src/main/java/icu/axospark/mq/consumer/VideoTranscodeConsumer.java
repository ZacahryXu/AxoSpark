package icu.axospark.mq.consumer;


import icu.axospark.pojo.message.VideoMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static icu.axospark.mq.config.RabbitMQ2VideoConfig.VIDEO_TRANSCODE_QUEUE;


@Component
@Slf4j
public class VideoTranscodeConsumer {

    @RabbitListener(queues = VIDEO_TRANSCODE_QUEUE,concurrency = "1-3")
    public void handleTranscodeTask(VideoMessage videoMessage){
        try {
            // 模拟视频转码处理
            log.info("正在转码视频: {})",
                    videoMessage.getVideoId());

            // 模拟耗时操作（实际应该调用转码服务）
            Thread.sleep(3000);

            // 转码完成
            log.info(">>>  视频转码完成 <<<");

            // 这里可以添加后续操作：
            // 1. 更新数据库状态
            // 2. 保存转码后的视频路径
            // 3. 发送通知给用户

        } catch (InterruptedException e) {
            log.error("视频转码被中断: {}", videoMessage.getVideoId(), e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("转码任务中断", e);
        } catch (Exception e) {
            log.error("视频转码失败: {}", videoMessage, e);
            // 这里可以实现重试逻辑或将失败任务发送到死信队列
            throw new RuntimeException("转码失败", e);
        }
    }
}
