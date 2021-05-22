package xyz.hapilemon.spring.validation.annotation;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Service
// 必须在类上添加@Validated注解 声明这个Bean需要进行参数校验
@Validated
@Data
public class PersonService extends BaseService {

    @NotBlank
    private String name;

    @Validated(value = {StepTwo.class})
    public void create(@Valid Person person) {


    }
}
