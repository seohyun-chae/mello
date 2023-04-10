package pjt.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisSampleService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setRedisStringValue(String key, String value){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(key, value);
        System.out.println("Redis key: " + key);
        System.out.println("Redis value: " + stringValueOperations.get(key));

    }

    public void setRedisHash() {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        Map<String, Object> map = new HashMap<>();
        map.put("id","value1");
        map.put("name","value2");
        map.put("password","value3");

//        hashOperations.putAll("user:"+입력받은아이디, map);

        String key1 = (String) stringRedisTemplate.opsForHash().get("brd:B002","brd_name");
        String key2 = (String) stringRedisTemplate.opsForHash().get("brd:B002","user_id");

        System.out.println(key1);
        System.out.println(key2);


    }
}
