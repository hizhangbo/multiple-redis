package io.github.hizhangbo.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "redis")
public class MultipleRedisConfiguration {

    private List<RedisProperties> list;

    public List<RedisProperties> getList() {
        return list;
    }

    public void setList(List<RedisProperties> list) {
        this.list = list;
    }
}
