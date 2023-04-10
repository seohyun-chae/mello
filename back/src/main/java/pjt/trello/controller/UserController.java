package pjt.trello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjt.trello.model.UserVO;
import pjt.trello.service.UserService;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserVO loginInfo) {
        log.info("--------------loginInfo:{}",loginInfo);

        return userService.login(loginInfo);
    }

    /**
     * 로그인 - 레디스 사용
     */
    @PostMapping("/loginR")
    public ResponseEntity loginR(@RequestBody UserVO loginInfo) {
        log.info("--------------loginInfo:{}",loginInfo);

        return userService.loginR(loginInfo);
    }
}
