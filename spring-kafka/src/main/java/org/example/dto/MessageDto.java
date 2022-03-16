package org.example.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author keminfeng
 * @date 2022-03-15 11:28
 */
@Data
public class MessageDto {

    private String topic;

    private String key;

    private Map<String, Object> body;

}
