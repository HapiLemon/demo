package org.example.util;

import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author keminfeng
 */
@Component
public class RedisLockUtil {

    @Autowired
    private RedissonClient redissonClient;
    
    public boolean lock(String key, String value) {
        String script = "local result = redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', '10000') if(result == false) then return 'FAIL' end if(result.ok == 'OK') then return 'SUCCESS' end";
        String result = redissonClient.getScript().eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.STATUS, List.of(key), value);
        return "SUCCESS".equals(result);
    }

    public boolean unlock(String key, String value) {
        String script = "local result = redis.call('get', KEYS[1]) if(result == ARGV[1]) then redis.call('del', KEYS[1]) return 'SUCCESS' end if(result == false) then return 'FAIL' end";
        String result = redissonClient.getScript().eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.STATUS, List.of(key), value);
        return "SUCCESS".equals(result);
    }
}

