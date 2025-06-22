package com.app.oneplace.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheManagerChecker {

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    public void logCacheManager() {
        System.out.println("Active CacheManager implementation: " + cacheManager.getClass().getName());
    }
}
