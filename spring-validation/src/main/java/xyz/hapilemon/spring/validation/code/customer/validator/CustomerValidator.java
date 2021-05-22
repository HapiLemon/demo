package xyz.hapilemon.spring.validation.code.customer.validator;

import io.ftlexpress.validate.code.customer.pojo.Address;
import io.ftlexpress.validate.code.customer.pojo.Customer;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomerValidator implements Validator {
    private final Validator addressValidator;

    public CustomerValidator(Validator addressValidator) {

        if (addressValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                    "required and must not be null.");
        }
        if (!addressValidator.supports(Address.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                    "support the validation of [Address] instances.");
        }
        this.addressValidator = addressValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "field.required");
        Customer customer = (Customer) target;
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
