package com.yato.urlshortener.repositories;


import com.yato.urlshortener.objects.RequestObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sun.misc.Request;

import java.util.Optional;

@Repository
public interface URLMappingsRepository extends JpaRepository<RequestObject, Long> {

    @Query("SELECT N FROM RequestObject N WHERE N.clientId = ?1")
    Optional<RequestObject> findRequestByUserId(String id);

    @Query("SELECT N FROM RequestObject N WHERE N.clientId = ?1 AND N.originalURL = ?2")
    Optional<RequestObject> findRequestByUserIdAndOriginalURL(String id, String originalURL);

    @Query("SELECT N FROM RequestObject N WHERE N.clientId = ?1 AND N.shortURL = ?2")
    Optional<RequestObject> findRequestByUserIdAndShortURL(String id, String shortURL);
}
