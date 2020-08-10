package com.tiny.url.controller;

import com.tiny.url.exception.BadRequestException;
import com.tiny.url.exception.EntityNotFoundException;
import com.tiny.url.model.HitUrlResponseEntity;
import com.tiny.url.model.UrlResponseEntity;
import com.tiny.url.service.TinyUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TinyUrlControllerTest {
    @Mock
    private TinyUrlRepository tinyUrlRepository;

    @InjectMocks
    private TinyUrlController tinyUrlController;

    @BeforeEach
    void setUp() {
        tinyUrlRepository = new TinyUrlRepository(null, null);
        initMocks(this);
    }

    @Test
    void testGetShortUrl() throws Exception {
        String shortUrl = "http://localhost:8345/tinyUrl/00000002";
        HashMap<String, String> payload = new HashMap<String, String>();
        payload.put("long_url", "http://www.google.com");
        payload.put("client_id", "12");

        when(tinyUrlRepository.getShortenedURL(payload.get("long_url"), payload.get("client_id"))).thenReturn(shortUrl);

        // Run the test
        ResponseEntity<UrlResponseEntity> result = tinyUrlController.getShortenedURL(payload);

        // Verify the results
        assertEquals(shortUrl, result.getBody().getUrl());
    }

    @Test
    void testGetShortUrlPayloadEmpty() throws Exception {
        String shortUrl = "http://localhost:8345/tinyUrl/00000002";
        HashMap<String, String> payload = new HashMap<String, String>();
        payload.put("long_url", "http://www.google.com");
        payload.put("client_id", "12");

        when(tinyUrlRepository.getShortenedURL(payload.get("long_url"), payload.get("client_id"))).thenReturn(shortUrl);

        // Run the test

        Throwable exception = assertThrows(BadRequestException.class, () -> tinyUrlController.getShortenedURL(new HashMap<String, String>()));
        assertEquals("Invalid long url", exception.getMessage());
    }

    @Test
    void testGetHitCount() throws Exception {
        HashMap<String, String> payload = new HashMap<String, String>();
        payload.put("short_url", "http://localhost:8345/tinyUrl/00000002");
        payload.put("client_id", "12");

        when(tinyUrlRepository.getHitCount(payload.get("short_url"))).thenReturn(2);

        // Run the test
        ResponseEntity<HitUrlResponseEntity> result = tinyUrlController.getHitCount(payload);

        // Verify the results
        assertEquals(2, result.getBody().getHitCount());
    }

    @Test
    void testGetHitCountWithInvalidURL() throws Exception {
        HashMap<String, String> payload = new HashMap<String, String>();
        payload.put("short_url", "http://localhost:8345/tinyUrl/00000002");
        payload.put("client_id", "12");

        when(tinyUrlRepository.getHitCount(payload.get("short_url"))).thenThrow(new EntityNotFoundException("Short url doesn't exist"));

        // Verify the results
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> tinyUrlController.getHitCount(payload));
        assertEquals("Short url doesn't exist", exception.getMessage());
    }

    @Test
    void testGetOriginalUrl() throws Exception {
        String short_url = "http://localhost:8345/tinyUrl/00000002";
        String long_url = "http://www.google.com";

        when(tinyUrlRepository.redirectURL(short_url)).thenReturn(long_url);

        ResponseEntity<Object> response = tinyUrlController.getOriginalURL(short_url);
        assertEquals(response.getStatusCode(), HttpStatus.SEE_OTHER);
    }

    @Test
    void testGetOriginalUrlInvalidURL() throws Exception {
        String short_url = "http://localhost:8345/tinyUrl/00000002";

        when(tinyUrlRepository.redirectURL(short_url)).thenThrow(new EntityNotFoundException("Short url doesn't exist"));

        // Verify the results
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> tinyUrlController.getOriginalURL(short_url));
        assertEquals("Short url doesn't exist", exception.getMessage());
    }
}