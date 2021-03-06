package xyz.hapilemon.jetcache.serialize;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.anno.SerialPolicy;

import java.util.Collection;
import java.util.function.Function;

public class JsonSerialPolicy implements SerialPolicy {

    @Override
    public Function<Object, byte[]> encoder() {
        return o -> {
            if (o != null) {
                CacheValueHolder cacheValueHolder = (CacheValueHolder) o;
                Object realObj = cacheValueHolder.getValue();
                String objClassName = realObj.getClass().getName();
                // 为防止出现 Value 无法强转成指定类型对象的异常，这里生成一个 JsonCacheObject 对象，保存目标对象的类型（比如 User）
                JsonCacheObject jsonCacheObject = new JsonCacheObject(objClassName, realObj);
                cacheValueHolder.setValue(jsonCacheObject);
                return JSONObject.toJSONString(cacheValueHolder).getBytes();
            }
            return new byte[0];
        };
    }

    @Override
    public Function<byte[], Object> decoder() {
        return bytes -> {
            if (bytes != null) {
                String str = new String(bytes);
                CacheValueHolder cacheValueHolder = JSONObject.parseObject(str, CacheValueHolder.class);
                JSONObject jsonObject = JSONObject.parseObject(str);
                // 首先要解析出 JsonCacheObject，然后获取到其中的 realObj 及其类型
                JSONObject jsonOfMy = jsonObject.getJSONObject("value");
                try {
                    Class<?> clazz = Class.forName(jsonOfMy.getString("className"));
                    // list对象直接进行反序列化时会报错 "cannot be cast to..."
                    // 所以进行一次判断
                    if (clazz.newInstance() instanceof Collection) {
                        JSONArray realObjOfJson = jsonOfMy.getJSONArray("realObj");
                        Object realObj = realObjOfJson.toJavaObject(clazz);
                        cacheValueHolder.setValue(realObj);
                    } else {
                        JSONObject realObjOfJson = jsonOfMy.getJSONObject("realObj");
                        Object realObj = realObjOfJson.toJavaObject(clazz);
                        cacheValueHolder.setValue(realObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return cacheValueHolder;
            }
            return null;
        };
    }
}