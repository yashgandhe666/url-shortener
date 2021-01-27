package com.yato.urlshortener.controllers;


import com.yato.urlshortener.exceptions.ShortURLNotPresentException;
import com.yato.urlshortener.objects.RequestObject;
import com.yato.urlshortener.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
public class MainController {
    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService service;

    @GetMapping(path = "/api/getShortURL")
    public ResponseEntity<String> generateShortURL(@RequestBody RequestObject obj) {
        String originalURL = obj.getOriginalURL();
        String clientId = obj.getClientId();
        if(originalURL == null || originalURL.equals("") || clientId == null || clientId.equals("")) {
            return new ResponseEntity<>("URL/Client Id missing.", HttpStatus.BAD_REQUEST);
        }
        if(obj.getExpiryTime() == null || obj.getExpiryTime() == 0) {
            logger.info("Expiry Time not present so setting to default time of 30 days");
            obj.setExpiryTime(30L);
        }
        String encodedURL = service.encodeURL(obj);
        return new ResponseEntity<>(encodedURL, HttpStatus.OK);
    }

    @GetMapping(path = "/api/getOriginalURL")
    public ResponseEntity<String> getOriginalURL(@RequestBody RequestObject obj) {
        String shortURL = obj.getShortURL();
        String clientId = obj.getClientId();
        if(shortURL == null || shortURL.equals("") || clientId == null || clientId.equals("")) {
            return new ResponseEntity<>("URL/Client Id missing.", HttpStatus.BAD_REQUEST);
        }
        String originalURL;
        try {
            originalURL = service.getOriginalURL(obj);
        } catch (ShortURLNotPresentException e) {
            logger.error("Short URL not present in database for clientId: {}", clientId);
            return new ResponseEntity<>(String.format("Original URL for the given short URL: %s not found", shortURL), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(originalURL, HttpStatus.OK);
    }

    // TODO: Handle requests based on clientId
    @GetMapping("/r/{id}")
    public Object localRedirect(@PathVariable String id, @RequestBody RequestObject obj) {
        RedirectView redirectView;
        try {
            redirectView = service.redirectURL(id, obj);
        } catch (ShortURLNotPresentException e) {
            logger.error("Short URL not present in database for clientId: {}", obj.getClientId());
            return new ResponseEntity<>("Original URL for the given short URL not found", HttpStatus.BAD_REQUEST);
        }
        return redirectView;
    }

}
