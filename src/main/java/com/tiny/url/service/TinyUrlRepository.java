package com.tiny.url.service;

import com.tiny.url.exception.BadRequestException;
import com.tiny.url.exception.EntityNotFoundException;
import com.tiny.url.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class TinyUrlRepository {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private KeyGenerator keyGenerator;

    public TinyUrlRepository(UrlRepository urlRepository, KeyGenerator keyGenerator) {
        this.urlRepository = urlRepository;
        this.keyGenerator = keyGenerator;
    }

    public String getShortenedURL(String long_url, String client_id) {
       Optional<Url> response = urlRepository.findByLongUrlAndClientId(long_url, client_id);
        if (response.isPresent()) {
            Url responseUrl = response.get();
            return "http://localhost:8345/tinyUrl/" + responseUrl.getShortUrl();
        }
        else {
            return createNewUrl(long_url, client_id);
        }
    }

    public String redirectURL(String short_url) {
        if(StringUtils.isEmpty(short_url)) {
            throw new BadRequestException("Short url is not in right format");
        }

        Optional<Url> response = urlRepository.findById(short_url);
        if(response.isPresent()) {
            Url url = response.get();
            url.setCount(url.getCount() + 1);
            urlRepository.save(url);
            return url.getLongUrl();
        }
        else {
            System.out.println("Entered not here");
            throw new EntityNotFoundException("Short url doesn't exist");
        }
    }

    public Integer getHitCount(String short_url) {
        String base62Encoded = short_url.substring(short_url.lastIndexOf("/") + 1);

        if(StringUtils.isEmpty(base62Encoded)) {
            throw new BadRequestException("Short url is not in right format");
        }
        Optional<Url> response = urlRepository.findById(base62Encoded);
        if(response.isPresent()) {
            return response.get().getCount();
        }
        else {
            throw new EntityNotFoundException("Short url doesn't exist");
        }
    }

    private String createNewUrl(String long_url, String client_id) {
        Url url = new Url();
        url.setClientId(client_id);
        url.setCount(0);
        url.setLongUrl(long_url);
        url.setShortUrl(keyGenerator.getKey());
        urlRepository.save(url);
        return "http://localhost:8345/tinyUrl/"+url.getShortUrl();
    }
}
