package org.example.productor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keminfeng
 * @date 2022-03-15 11:09
 */
@RestController
@RequestMapping("/api/kafka/producer")
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public void product(@RequestBody MessageDto messageDto) throws JsonProcessingException {
        kafkaTemplate.send(messageDto.getTopic(), messageDto.getKey(), new ObjectMapper().writeValueAsString(messageDto));
    }

}
