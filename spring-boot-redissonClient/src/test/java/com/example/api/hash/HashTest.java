package com.example.api.hash;

import com.example.redisson.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.MapOptions;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HashTest {


    @Autowired
    private RedisCache redisCache;

    /**
     * redisson client对象
     */
    @Autowired
    private RedissonClient redisson;

    @Test
    public void test2() throws Exception {
        String key = "hash.12345566777";
        MapLoader<String, Object> mapLoader = new MapLoader<String, Object>() {
            @Override
            public Iterable<String> loadAllKeys() {
                List<String> list = new ArrayList<>();
                list.add("0011");
                list.add("0012");
                list.add("0013");
                return list;
            }

            @Override
            public String load(String key) {
                return "0020";
            }
        };

        MapOptions<String, Object> options = MapOptions.<String, Object>defaults()
                .loader(mapLoader);
        RMap<String, Object> map = redisson.getMap(key, options);
        System.out.println(map.size());
        System.out.println("success");
    }


    /**
     *
     */
    @Test
    public void test1() throws Exception {
        String key = "hash.12345566";
        RMapCache<String, String> hashMap = redisCache.getRedisMap(key);

        hashMap.expire(1, TimeUnit.HOURS);
        hashMap.put("001", "001-value");
        //hashMap.put("002", "002-value", 50, TimeUnit.SECONDS);
        System.out.println("hashMap size=" + hashMap.size());

        System.out.println(hashMap.get("001"));
        System.out.println(hashMap.get("002"));
//        for (Map.Entry<String, String> map : hashMap.entrySet()) {
//            System.out.println("key: " + map.getKey() + "  value:" + map.getValue());
//        }

        for (int i = 1; i <= 100; i++) {
            hashMap.put("001" + i, "001-value" + i);
        }

    }
}