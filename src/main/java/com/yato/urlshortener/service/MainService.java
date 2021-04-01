package com.yato.urlshortener.service;


import com.devskiller.friendly_id.FriendlyId;
import com.yato.urlshortener.exceptions.ShortURLNotPresentException;
import com.yato.urlshortener.objects.RequestObject;
import com.yato.urlshortener.repositories.URLMappingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.yato.urlshortener.constants.GlobalConstants.BASE_URL;

@Service
public class MainService {

    @Autowired
    URLMappingsRepository urlMappingsRepository;

    Logger logger = LoggerFactory.getLogger(MainService.class);
    static int counter = 0;

    public String encodeURL(RequestObject obj) {
        Optional<RequestObject> storedObj = urlMappingsRepository.findRequestByUserIdAndOriginalURL(obj.getClientId(), obj.getOriginalURL());
        String encodedURL = "";
        if(storedObj.isPresent()) {
            logger.info("URL already present in database. Returning value");
            encodedURL = storedObj.get().getShortURL();
        } else {
            logger.info("URL not present in database for the clientId: {}. Generating new value", obj.getClientId());
            Long currentTime = System.currentTimeMillis();
            Long expiryTime = TimeUnit.DAYS.toMillis(obj.getExpiryTime());
            // Added clientId to originalURL to generate new shortURL for different clientId with same originalURL
            encodedURL = urlBuilder(encoder(obj.getOriginalURL() + obj.getClientId()));
            obj.setShortURL(encodedURL);
            obj.setRedirectionCount(0L);
            obj.setEntryAdditionTime(currentTime);
            obj.setExpiryTime(expiryTime);
            urlMappingsRepository.save(obj);
        }

        return encodedURL;
    }

    public String urlBuilder(String identifier) {
        return BASE_URL.concat("/r/").concat(identifier);
    }

    public String encoder(String value) {
        String updatedValue = value.concat(String.valueOf(counter));
        counter+=1;
        UUID id = UUID.nameUUIDFromBytes(updatedValue.getBytes());
        return FriendlyId.toFriendlyId(id).substring(0,8);
    }

    public String getOriginalURL(RequestObject obj) throws ShortURLNotPresentException {
        Optional<RequestObject> storedObj = urlMappingsRepository.findRequestByUserIdAndShortURL(obj.getClientId(), obj.getShortURL());

        if(storedObj.isPresent()) {
            logger.info("Original URL present in database. Returning value");
            return storedObj.get().getOriginalURL();
        } else {
            throw new ShortURLNotPresentException("Short URL not present in database for clientId: " + obj.getClientId());
        }
    }

    public RedirectView redirectURL(String urlIdentifier) throws ShortURLNotPresentException {

        String shortURL = urlBuilder(urlIdentifier);
        Optional<RequestObject> storedObj = urlMappingsRepository.findRequestByShortURL(shortURL);
        if(storedObj.isPresent()) {
            logger.info("Original URL present in database. Incrementing counter, and redirecting");
            String clientId = storedObj.get().getClientId();
            urlMappingsRepository.updateRedirectionCount(storedObj.get().getRedirectionCount()+1, clientId, shortURL);

            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(storedObj.get().getOriginalURL());

            return redirectView;
        } else {
            throw new ShortURLNotPresentException("Short URL not present in database");
        }
    }

}