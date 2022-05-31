package com.fentiaozi.common;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

/**
 * @author fentiaozi
 */
@Configuration
public class RedissonConfig {

    @Value(value = "${fentiaozi.redis.cluster.open}")
    private Boolean isOpen;

    @Value(value = "${spring.redis.host}")
    private String redisHost;
    @Value(value = "${spring.redis.port}")
    private String redisPort;

    @Autowired
    private Environment environment;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {

        String[] profiles = environment.getActiveProfiles();
        String profile = "";
        if (profiles.length > 0) {
            profile = "-" + profiles[0];

        }
        Config config;
        if (Boolean.TRUE.equals(isOpen)) {
            //集群redisson
            config = Config.fromJSON(RedissonConfig.class.getClassLoader().getResource("redisson" + profile + ".yml"));
        } else {
            //单机redisson
            config = new Config();
            config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
        }

        return Redisson.create(config);
    }
}
