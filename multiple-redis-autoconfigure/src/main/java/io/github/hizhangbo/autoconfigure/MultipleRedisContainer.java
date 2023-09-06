package io.github.hizhangbo.autoconfigure;

import io.github.hizhangbo.manager.RedisManager;
import io.github.hizhangbo.manager.RedisTemplateWrapper;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MultipleRedisContainer {

    private final MultipleRedisConfiguration multipleRedisConfiguration;
    private final ConfigurableBeanFactory beanFactory;

    private final List<RedisTemplateWrapper> redisTemplateWrapperList = new ArrayList<>();

    public MultipleRedisContainer(MultipleRedisConfiguration multipleRedisConfiguration,
                                  ConfigurableBeanFactory beanFactory) {
        this.multipleRedisConfiguration = multipleRedisConfiguration;
        this.beanFactory = beanFactory;
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

                String beanNameRedisTemplate = redisProperties.getName() + "RedisTemplate" + database;
                beanFactory.registerSingleton(beanNameRedisTemplate, redisTemplateWrapper.getRedisTemplate());
            }
        }
    }

    @Bean
    public RedisManager redisManager() {
        return new RedisManager(redisTemplateWrapperList);
    }
}
