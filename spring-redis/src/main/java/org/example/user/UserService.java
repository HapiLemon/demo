package org.example.user;

import org.example.util.RedisLockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author keminfeng
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Cacheable(value = "user.name", key = "#name")
    public User findByName(String name) {
        return userDao.findByName(name);
    }

    public User findByNameWithLock(String name) throws InterruptedException {
        User user = null;
        // 获取锁
        RLock lock = this.redissonClient.getLock(name);
        boolean getLockFlag = lock.tryLock(10, 5, TimeUnit.SECONDS);

        if (getLockFlag) {
            // 完成动作
            try {
                user = this.findByName(name);
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
        return user;
    }

    public User findByNameWithMyLock(String name) {
        boolean lock;
        User user;
        do {
            lock = this.redisLockUtil.lock(name, name);
        } while (!lock);
        try {
            user = this.findByName(name);
        } finally {
            this.redisLockUtil.unlock(name, name);
        }
        return user;
    }
}
