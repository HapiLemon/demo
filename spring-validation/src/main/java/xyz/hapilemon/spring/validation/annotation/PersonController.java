package xyz.hapilemon.spring.validation.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/person")
    public ResponseEntity<Person> create(@Valid Person person, BindingResult bindingResult) {
        System.out.println(bindingResult);
        return null;
    }

    @GetMapping("/person")
    public void create() {
        personService.create(new Person());
    }

}
