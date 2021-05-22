package xyz.hapilemon.spring.validation.annotation;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class Person {

    @NotNull(message = "classId 不能为空")
    private String classId;

    @Size(max = 33)
    @NotNull(message = "name 不能为空")
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围")
    @NotNull(message = "sex 不能为空", groups = {StepTwo.class})
    private String sex;

    @Email(message = "email 格式不正确")
    @NotNull(message = "email 不能为空")
    private String email;

}
//    @Valid
//    @NotNull
//    private List<Info> infoList;
//
//    @Valid
//    private Info info;
//
//}
//
//@Data
//class Info{
//
//    @NotBlank
//    private String name;
//
//}
