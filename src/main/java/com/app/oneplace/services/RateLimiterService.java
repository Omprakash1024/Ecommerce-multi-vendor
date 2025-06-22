package com.app.oneplace.services;

import java.time.Duration;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedissonClient redissonClient;

    public boolean tryConsumeToken(String clientKey, int limit, int refillTimeInSeconds) {
        RBucket<Integer> bucket = redissonClient.getBucket(clientKey);

        if (!bucket.isExists()) {
            bucket.set(limit - 1, Duration.ofSeconds(refillTimeInSeconds));
            return true;
        }

        Integer tokensLeft = bucket.get();
        if (tokensLeft != null && tokensLeft > 0) {
            bucket.set(tokensLeft - 1, Duration.ofSeconds(refillTimeInSeconds));
            return true;
        }

        return false;
    }
}
