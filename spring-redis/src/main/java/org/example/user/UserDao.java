package org.example.user;

import lombok.extern.slf4j.Slf4j;
import org.example.Sleep;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author keminfeng
 */
@Slf4j
@Component
public class UserDao implements Sleep {

    @Cacheable(value = "user.name", key = "#name")
    public User findByName(String name) {
        log.info("SELECT * FROM user WHERE name = '" + name + "' LIMIT 1;");
        sleep(200L);
        User user = new User();
        user.setName(name);
        return user;
    }

}
