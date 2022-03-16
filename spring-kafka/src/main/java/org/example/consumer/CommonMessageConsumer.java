package org.example.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author keminfeng
 * @date 2022-03-16 10:35
 */
@Component
public class CommonMessageConsumer implements MessageListener<String, String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        System.out.println(data);
    }
}
