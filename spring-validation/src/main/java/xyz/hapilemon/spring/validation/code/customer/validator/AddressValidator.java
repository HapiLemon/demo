package xyz.hapilemon.spring.validation.code.customer.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import xyz.hapilemon.spring.validation.code.customer.pojo.Address;

public class AddressValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Address.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "location", "location.empty");
        Address p = (Address) obj;
        if (p != null && p.getLocation() != null && p.getLocation().length() > 5) {
            e.rejectValue("location", "value too length", "长度不能超过5");
        }
    }
}
