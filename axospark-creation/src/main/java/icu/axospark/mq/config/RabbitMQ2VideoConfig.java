package icu.axospark.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ2VideoConfig {
    //视频转码队列名称
    public static final String VIDEO_TRANSCODE_QUEUE  = "video.transcode.queue";

    /**
     * 创建持久化队列
     * durable = true
     * @return
     */
    @Bean
    public Queue videoTranscodeQueue(){
        return new Queue(VIDEO_TRANSCODE_QUEUE,true);
    }
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
