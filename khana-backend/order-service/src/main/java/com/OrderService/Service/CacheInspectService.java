package com.OrderService.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheInspectService {

    @Autowired
    private CacheManager cacheManager;

    public void inspectCache(String userEmail) {
        Cache cache = cacheManager.getCache("orderHistory");
        if (cache == null) {
            System.out.println("Cache 'orderHistory' does not exist yet.");
            return;
        }
        Cache.ValueWrapper value = cache.get(userEmail);
        if (value == null) {
            System.out.println("No entry found for key: " + userEmail);
        } else {
            System.out.println("Cache HIT for: " + userEmail);
            System.out.println("Cached value: " + value.get());
        }

    }

}
