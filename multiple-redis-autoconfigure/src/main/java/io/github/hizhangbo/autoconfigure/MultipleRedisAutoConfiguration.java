package io.github.hizhangbo.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Import(MultipleRedisContainer.class)
@EnableConfigurationProperties(MultipleRedisConfiguration.class)
public class MultipleRedisAutoConfiguration {
}
