package com.tiny.url.service;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyGenerator {
    String base62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Long counter = 1L;

    public KeyGenerator() {
    }

    public String getKey() {
        String shortKey = base62Encode(this.counter);
        incrementCounter();
        return shortKey;
    }

    private String base62Encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value != 0) {
            sb.append(base62.charAt((int)(value % 62)));
            value /= 62;
        }
        while (sb.length() < 8) {
            sb.append(0);
        }
        return sb.reverse().toString();
    }

    public void incrementCounter() {
        this.counter = this.counter + 1;
    }
}
