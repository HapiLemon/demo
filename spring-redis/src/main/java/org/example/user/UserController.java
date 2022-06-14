package org.example.user;

import org.example.Sleep;
import org.example.util.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author keminfeng
 */

@RestController
@RequestMapping("/api/user")
public class UserController implements Sleep {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @GetMapping("/name/{name}")
    public User findByName(@PathVariable("name") String name) {
        return userService.findByName(name);
    }

    @GetMapping("/lock")
    public List<User> testRedissonLock() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String name = "name" + i / 10;
            new Thread(() -> {
                User user = null;
                try {
                    user = this.userService.findByNameWithLock(name);
                } catch (InterruptedException ignore) {
                }
                userList.add(user);
            }).start();
        }
        sleep(3000L);
        return userList;
    }

    @GetMapping("/myLock")
    public List<User> testMyLock() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String name = "name" + i / 10;
            new Thread(() -> {
                User user = this.userService.findByNameWithMyLock(name);
                userList.add(user);
            }).start();
        }
        sleep(3000L);
        return userList;
    }

}
