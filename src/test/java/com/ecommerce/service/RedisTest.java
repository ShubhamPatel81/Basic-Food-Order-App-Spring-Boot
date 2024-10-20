package com.ecommerce.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testSendMail() {
        // Setting a value in Redis
        redisTemplate.opsForValue().set("email", "shubhampatel872005@gmail.com");
        
        // Retrieving the value from Redis
        Object email = redisTemplate.opsForValue().get("email");
        
        // Assertion to ensure the value retrieved matches what was set
        assertEquals("shubhampatel872005@gmail.com", email);
        
        // For debugging purposes (optional)
        System.out.println("Stored email in Redis: " + email);
    }
}
