package org.example.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keminfeng
 * @date 2022-03-15 14:06
 */
@Component
@RestController
public class MessageConsumer {

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Autowired
    private CommonMessageConsumer commonMessageConsumer;

    //    @KafkaListener(id = "COMMON_LISTENER", topics = "#{'TOPIC_1,TOPIC_2'.split(',')}",groupId = "SPRING_KAFKA_DEMO")
    public void listener(String data) throws JsonProcessingException, InterruptedException {
        MessageDto messageDto = new ObjectMapper().readValue(data, MessageDto.class);
        if ("abc".equals(messageDto.getKey())) {
            Thread.sleep(10000);
        }
        System.out.println(messageDto);
    }

    //    @KafkaListener(id = "COMMON_LISTENER", topics = "#{'TOPIC_1,TOPIC_2'.split(',')}",groupId = "SPRING_KAFKA_DEMO")
    public void listener2(String data) throws JsonProcessingException, InterruptedException {
        MessageDto messageDto = new ObjectMapper().readValue(data, MessageDto.class);
        if ("TOPIC_2".equals(messageDto.getTopic())) {
            Thread.sleep(10000);
        }
        System.out.println(messageDto);
    }

    //    @Bean
    public ConcurrentMessageListenerContainer<String, String> listener() {
        ConcurrentMessageListenerContainer<String, String> concurrentMessageListenerContainer = new ConcurrentMessageListenerContainer<>(
                consumerFactory,
                new ContainerProperties("TOPIC_1"));
        concurrentMessageListenerContainer.setupMessageListener(commonMessageConsumer);
        concurrentMessageListenerContainer.setConcurrency(2);
        // 即使不交给容器管理 也可以完成功能
        // 作为一个Bean放进容器后 会调用Bean的start()方法
        // 但使用容器的话 就需要手动调用这个方法去start()
        return concurrentMessageListenerContainer;
//        concurrentMessageListenerContainer.start();
//        return null;
    }

    /**
     * 在服务启动后再增加一些TOPIC
     * https://stackoverflow.com/a/48022114
     */
    @PostMapping("/addTopic")
    public void addTopic() {
        ConcurrentMessageListenerContainer<String, String> concurrentMessageListenerContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory, new ContainerProperties("TOPIC_2"));
        concurrentMessageListenerContainer.setupMessageListener(commonMessageConsumer);
        concurrentMessageListenerContainer.start();
    }

}
