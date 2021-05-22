package xyz.hapilemon.spring.validation.code.person;

import io.ftlexpress.validate.code.person.pojo.Person;
import io.ftlexpress.validate.code.person.validator.PersonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/code/person")
public class PersonController {

    @PostMapping
    public void savePerson(@RequestBody Person person) {
        DataBinder dataBinder = new DataBinder(person);
        dataBinder.setValidator(new PersonValidator());

        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        log.info("result:{}", bindingResult);
    }

}
