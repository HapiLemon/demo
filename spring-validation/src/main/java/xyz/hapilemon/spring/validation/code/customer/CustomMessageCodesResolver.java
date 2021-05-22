package xyz.hapilemon.spring.validation.code.customer;

import org.springframework.stereotype.Component;
import org.springframework.validation.MessageCodesResolver;

import java.io.Serializable;

/**
 * 尝试自定义返回消息的格式，但是我拉闸了
 */
@Component("messageCodesResolver")
public class CustomMessageCodesResolver implements MessageCodesResolver, Serializable {
    @Override
    public String[] resolveMessageCodes(String errorCode, String objectName) {
        return this.resolveMessageCodes(errorCode, objectName, "", null);
    }

    @Override
    public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
        System.out.println(11111);
        return new String[0];
    }
}
