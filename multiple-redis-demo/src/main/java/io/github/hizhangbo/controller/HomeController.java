package io.github.hizhangbo.controller;

import io.github.hizhangbo.manager.RedisManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class HomeController {

    private final RedisManager redisManager;

    public HomeController(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    @GetMapping
    public String index() {
        final RedisTemplate<String, String> redisTemplate = redisManager.redisTemplate("redis2", 1);

        redisTemplate.opsForValue().set("TEST", "123456", 10, TimeUnit.MINUTES);

        final String test = redisTemplate.opsForValue().get("TEST");
        System.out.println(test);

        final RedisTemplate<String, String> redisTemplate2 = redisManager.redisTemplate(2, 0);

        redisTemplate2.opsForValue().set("TEST2", "654321", 10, TimeUnit.MINUTES);

        final String test2 = redisTemplate2.opsForValue().get("TEST2");
        System.out.println(test2);

        return "index";
    }
}
