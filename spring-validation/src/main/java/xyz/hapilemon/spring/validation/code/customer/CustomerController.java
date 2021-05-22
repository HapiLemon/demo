package xyz.hapilemon.spring.validation.code.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hapilemon.spring.validation.code.customer.pojo.Customer;
import xyz.hapilemon.spring.validation.code.customer.validator.AddressValidator;
import xyz.hapilemon.spring.validation.code.customer.validator.CustomerValidator;

@Slf4j
@RestController
@RequestMapping("/code/customer")
public class CustomerController {

    @PostMapping
    public void saveCustomer(@RequestBody Customer customer) {
        DataBinder dataBinder = new DataBinder(customer);
        dataBinder.setValidator(new CustomerValidator(new AddressValidator()));

        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        log.info("result:{}", bindingResult);
    }

}

