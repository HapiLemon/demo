package xyz.hapilemon.spring.validation.code.customer.pojo;

import lombok.Data;

@Data
public class Customer {

    private String firstName;
    private String surname;

    private Address address;
}