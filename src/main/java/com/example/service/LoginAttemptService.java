package com.example.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final Cache<String, Integer> attemptsCache;

    public LoginAttemptService() {

        this.attemptsCache = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build();
    }

    // Метод для фіксації невдалої спроби
    public void loginFailed(String ip) {
        int attempts = attemptsCache.getIfPresent(ip) != null ? attemptsCache.getIfPresent(ip) : 0;
        attemptsCache.put(ip, attempts + 1);
    }

    // Метод для перевірки, чи заблокований IP
    public boolean isBlocked(String ip) {
        Integer attempts = attemptsCache.getIfPresent(ip);
        return attempts != null && attempts >= 3;
    }

    // Метод для очищення спроб після успішного входу
    public void loginSucceeded(String ip) {
        attemptsCache.invalidate(ip);
    }
}
