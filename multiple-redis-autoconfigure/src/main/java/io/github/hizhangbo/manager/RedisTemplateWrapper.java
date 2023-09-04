package io.github.hizhangbo.manager;

import io.github.hizhangbo.autoconfigure.RedisProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

public class RedisTemplateWrapper {

    private String name;
    private Integer index;
    private Integer database;
    private RedisTemplate<String, String> redisTemplate;

    public RedisTemplateWrapper(RedisProperties redisProperties, int database) {
        this.name = redisProperties.getName();
        this.index = redisProperties.getIndex();
        this.database = database;

        createRedisTemplate(redisProperties);
    }

    public void createRedisTemplate(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(Integer.parseInt(String.valueOf(redisProperties.getPort())));
        configuration.setDatabase(database);
        String password = redisProperties.getPassword();
        if (password != null && !"".equals(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }

        GenericObjectPoolConfig<RedisProperties.Pool> genericObjectPoolConfig = new GenericObjectPoolConfig<>();

        RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
        genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
        genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
        genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
        genericObjectPoolConfig.setMaxWait(pool.getMaxWait());

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        Duration shutdownTimeout = redisProperties.getLettuce().getShutdownTimeout();
        if (shutdownTimeout != null) {
            builder.shutdownTimeout(shutdownTimeout);
        }
        LettuceClientConfiguration clientConfiguration = builder.poolConfig(genericObjectPoolConfig).build();


        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, clientConfiguration);
        connectionFactory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);

        redisTemplate.afterPropertiesSet();
    }


    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }
}
