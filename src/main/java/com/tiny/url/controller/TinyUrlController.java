package com.tiny.url.controller;

import com.tiny.url.exception.BadRequestException;
import com.tiny.url.model.HitUrlResponseEntity;
import com.tiny.url.model.UrlResponseEntity;
import com.tiny.url.service.TinyUrlRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TinyUrlController {

    @Autowired
    private TinyUrlRepository repository;

    @ApiOperation(value = "Take long URL and return a much shorter URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respond with short URL"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
        }
    )
    @RequestMapping(
            value = "/getShortenedURL",
            method = POST,
            headers = "Accept=application/json")
    ResponseEntity<?> getShortenedURL(@RequestBody Map<String, String> payload) throws Exception {
        String long_url = payload.get("long_url");
        String client_id = payload.get("client_id");
        if (StringUtils.isEmpty(long_url)) {
            throw new BadRequestException("Invalid long url");
        }
        if (StringUtils.isEmpty(client_id)) {
        throw new BadRequestException("Invalid client id");
        }
        String short_url = repository.getShortenedURL(long_url, client_id);

        UrlResponseEntity urlResponseEntity = new UrlResponseEntity("SUCCESS", short_url);
        return new ResponseEntity<>(urlResponseEntity, HttpStatus.OK);
    }

    @ApiOperation(value = "Take long URL and return a much shorter URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respond with short URL"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    }
    )
    @RequestMapping(
            value = "/getHitCount",
            method = POST,
            headers = "Accept=application/json")
    ResponseEntity<?> getHitCount(@RequestBody Map<String, String> payload) throws Exception {
        String short_url = payload.get("short_url");

        if (StringUtils.isEmpty(short_url)) {
            throw new BadRequestException("Invalid short url");
        }
        Integer hitCount = repository.getHitCount(short_url);

        HitUrlResponseEntity hitUrlResponseEntity = new HitUrlResponseEntity("SUCCESS", hitCount);
        return new ResponseEntity<>(hitUrlResponseEntity, HttpStatus.OK);
    }

    @ApiOperation(value = "Take long URL and return a much shorter URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respond with short URL"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    }
    )
    @RequestMapping(
            value = "/tinyUrl/{shortUrl}",
            method = GET,
            headers = "Accept=application/json")
    ResponseEntity<Object> getOriginalURL(@PathVariable("shortUrl") String short_url) throws Exception {
        if (StringUtils.isEmpty(short_url)) {
            throw new BadRequestException("Invalid short url");
        }

        String longUrl = repository.redirectURL(short_url);

        URI url = new URI(longUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(url);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}