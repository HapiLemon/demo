package xyz.hapilemon.j2cache.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private Long id;
    private String username;
    private String password;
}
