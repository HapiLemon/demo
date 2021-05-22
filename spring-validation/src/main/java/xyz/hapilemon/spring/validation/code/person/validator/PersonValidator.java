package xyz.hapilemon.spring.validation.code.person.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import xyz.hapilemon.spring.validation.code.person.pojo.Person;

public class PersonValidator implements Validator {

    /**
     * This Validator validates only Person instances
     */
    @Override
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        Person p = (Person) obj;
        if (p.getAge() < 0) {
            e.rejectValue("age", "negative value");
        } else if (p.getAge() > 110) {
            e.rejectValue("age", "too.darn.old");
        }
    }
}
