package io.github.hizhangbo.autoconfigure;

import io.github.hizhangbo.manager.RedisManager;
import io.github.hizhangbo.manager.RedisTemplateWrapper;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MultipleRedisContainer {

    private final MultipleRedisConfiguration multipleRedisConfiguration;

    private final List<RedisTemplateWrapper> redisTemplateWrapperList = new ArrayList<>();

    public MultipleRedisContainer(MultipleRedisConfiguration multipleRedisConfiguration) {
        this.multipleRedisConfiguration = multipleRedisConfiguration;
    }

    @PostConstruct
    public void init() {
        final List<RedisProperties> list = multipleRedisConfiguration.getList();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (RedisProperties redisProperties : list) {
            for (Integer database : redisProperties.getDatabases()) {
                RedisTemplateWrapper redisTemplateWrapper = new RedisTemplateWrapper(redisProperties, database);
                redisTemplateWrapperList.add(redisTemplateWrapper);
            }
        }
    }

    @Bean
    public RedisManager redisManager() {
        return new RedisManager(redisTemplateWrapperList);
    }
}
