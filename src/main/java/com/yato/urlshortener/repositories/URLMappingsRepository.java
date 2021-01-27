package com.yato.urlshortener.repositories;


import com.yato.urlshortener.objects.RequestObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface URLMappingsRepository extends JpaRepository<RequestObject, Long> {

    @Query("SELECT N FROM RequestObject N WHERE N.clientId = ?1")
    Optional<RequestObject> findRequestByUserId(String id);

    @Query("SELECT N FROM RequestObject N WHERE N.clientId = ?1 AND N.originalURL = ?2")
    Optional<RequestObject> findRequestByUserIdAndOriginalURL(String id, String originalURL);

    @Query("SELECT N FROM RequestObject N WHERE N.clientId = ?1 AND N.shortURL = ?2")
    Optional<RequestObject> findRequestByUserIdAndShortURL(String id, String shortURL);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("UPDATE RequestObject N SET N.redirectionCount = ?1 WHERE N.clientId = ?2 AND N.shortURL = ?3")
    void updateRedirectionCount(Long redirectionCount, String id, String shortURL);

    @Modifying
    @Transactional
    @Query("DELETE FROM RequestObject WHERE entryAdditionTime + expiryTime <= ?1")
    Integer deleteExpiredEntries(Long timestamp);

}
