package com.yato.urlshortener.service;

import com.yato.urlshortener.repositories.URLMappingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ExpiryCheckService {

    @Autowired
    URLMappingsRepository urlMappingsRepository;

    Logger logger = LoggerFactory.getLogger(ExpiryCheckService.class);

    @Scheduled(cron = "0 0 0 * * *")
    private void checkExpiredEntries() {
        Long currentTime = System.currentTimeMillis();
        logger.info("checkExpiredEntries() called at {}", currentTime);
        Integer storedObj = urlMappingsRepository.deleteExpiredEntries(currentTime);
        logger.info("Count of expired events present: {}. Deleting expired events", storedObj);
    }


}
