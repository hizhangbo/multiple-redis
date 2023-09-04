package io.github.hizhangbo.manager;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisManager {

    private final List<RedisTemplateWrapper> redisTemplateWrapperList;

    public RedisManager(List<RedisTemplateWrapper> redisTemplateWrapperList) {
        this.redisTemplateWrapperList = redisTemplateWrapperList;
    }

    public RedisTemplate<String, String> redisTemplate(String name, int database) {
        return redisTemplateWrapperList
                .stream()
                .filter(r -> name.equals(r.getName()) && r.getDatabase() == database)
                .findFirst()
                .map(RedisTemplateWrapper::getRedisTemplate)
                .orElseThrow(IllegalStateException::new);
    }

    public RedisTemplate<String, String> redisTemplate(int index, int database) {
        return redisTemplateWrapperList
                .stream()
                .filter(r -> index == r.getIndex() && r.getDatabase() == database)
                .findFirst()
                .map(RedisTemplateWrapper::getRedisTemplate)
                .orElseThrow(IllegalStateException::new);
    }
}
