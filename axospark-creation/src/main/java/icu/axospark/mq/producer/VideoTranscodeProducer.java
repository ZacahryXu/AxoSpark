package icu.axospark.mq.producer;

import icu.axospark.pojo.message.VideoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static icu.axospark.mq.config.RabbitMQ2VideoConfig.VIDEO_TRANSCODE_QUEUE;

@Component
@Slf4j
public class VideoTranscodeProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendTranscodeTask(VideoMessage videoMessage){
        try{
            rabbitTemplate.convertAndSend(
                    VIDEO_TRANSCODE_QUEUE,
                    videoMessage
            );
            log.info("视频转码任务已发送到队列: {}", videoMessage);
        }catch (Exception e){
            log.error("发送视频转码任务失败: {}",videoMessage,e);
            throw new RuntimeException("发送转码任务失败",e);
        }

    }
}
