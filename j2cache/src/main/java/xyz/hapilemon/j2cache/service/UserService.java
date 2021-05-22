package xyz.hapilemon.j2cache.service;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.hapilemon.j2cache.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "demo.user")
public class UserService {

    @Autowired
    private Map<Long, User> userMap;

    @Cacheable(key = "'user:' + #user.id", unless = "#result == null")
    public User create(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Cacheable(key = "'user:' + #id")
    public User findById(Long id) {
        return userMap.get(id);
    }

    @Cacheable(value = "all", unless = "#result == null")
    public List<User> findAll() {
        return new ArrayList(userMap.values());
    }

    //    @CachePut( key = "'user:'+#user.id", unless = "#result == null")
//    public void update(User user) {
    public User update(User user) {
        userMap.put(user.getId(), user);
        CacheChannel channel = J2Cache.getChannel();
        channel.set("demo.user", "user:" + user.getId(), user);
        return user;
    }

    @CacheEvict(value = "demo.user", allEntries = true)
    public void delete(Long id) {
        userMap.remove(id);
    }

    @Cacheable(value = "map", unless = "#result == null")
    public Map<Long, User> getUserMap() {
        return userMap;
    }
}