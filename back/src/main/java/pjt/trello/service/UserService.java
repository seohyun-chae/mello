package pjt.trello.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjt.trello.model.UserVO;
import pjt.trello.repository.UserDAO;

import java.util.HashMap;
import java.util.Set;

import static io.lettuce.core.StrAlgoArgs.Builder.keys;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //로그인
    public ResponseEntity login(UserVO loginInfo){
        String loginId = loginInfo.getUserId();
        String loginPw = loginInfo.getPassword();
        UserVO userVO = userDAO.getUser(loginId, loginPw);
        HashMap<String, Object> map = new HashMap<>();

        if(userVO != null) {
            map.put("loginY",true);
            map.put("loginId", loginId);
        }else {
            map.put("loginY",false);
            map.put("loginId", "Failed");
        }

        return ResponseEntity.ok(map);
    }

    //로그인 - 레디스
    public ResponseEntity loginR(UserVO loginInfo){
        String loginId = loginInfo.getUserId();
        String loginPw = loginInfo.getPassword();

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        // 데이터 조회
        String key = "user:"+loginId;
        System.out.println(key);
        String result = (String) redisTemplate.opsForHash().get(key,"user_name");
        String pwd = (String) redisTemplate.opsForHash().get(key,"password");

        System.out.println("-------------result");
        System.out.println("-------------pwd");
        System.out.println(result);
        System.out.println(pwd);

        HashMap<String, Object> map = new HashMap<>();

        if((result != null)&&(loginPw.equals(pwd))) {
            map.put("loginY",true);
            map.put("loginId", loginId);
        }else {
            map.put("loginY",false);
            map.put("loginId", "Failed");
        }

        return ResponseEntity.ok(map);
    }

}
