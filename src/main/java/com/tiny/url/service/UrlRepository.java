package com.tiny.url.service;

import com.tiny.url.model.Url;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, String> {

    Optional<Url> findByLongUrlAndClientId(String longUrl, String clientId);

}
