package xyz.hapilemon.j2cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import xyz.hapilemon.j2cache.pojo.User;

import java.util.HashMap;
import java.util.Map;

@EnableCaching
@SpringBootApplication
public class J2CacheDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(J2CacheDemoApplication.class);
    }

    @Bean
    public Map<Long, User> userMap() {
        return new HashMap<>();
    }
}
