package xyz.hapilemon.spring.validation.annotation;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface PersonServiceIF {
    void create(@Valid @NotNull Person person);
}
