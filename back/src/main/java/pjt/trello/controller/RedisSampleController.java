package pjt.trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pjt.trello.service.RedisSampleService;

@RestController
public class RedisSampleController {
    @Autowired
    private RedisSampleService redisSampleService;

//    @PostMapping("/setRedisStringValue")
//    public void getRedisStringValue(String key, String value){
//        redisSampleService.setRedisStringValue(key, value);
//    }

    @PostMapping("/setRedisHash")
    public void setRedisHash() { redisSampleService.setRedisHash(); }

//    @GetMapping("/getRedisHash")
//    public void getRedisHash() { redisSampleService.getRedisHash(); }
}
