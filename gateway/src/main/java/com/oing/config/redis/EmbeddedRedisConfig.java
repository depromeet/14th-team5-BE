package com.oing.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.RedisConnectionFailureException;
import redis.embedded.RedisServer;

import java.io.File;

@Configuration
@Profile("local")
@Slf4j
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        try {
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory 256M")
                    .build();
            redisServer.start();

        } catch (RuntimeException e) { // Run redis manually
            redisServer = new RedisServer(new File("/usr/local/bin/redis-server"), port);
            redisServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
