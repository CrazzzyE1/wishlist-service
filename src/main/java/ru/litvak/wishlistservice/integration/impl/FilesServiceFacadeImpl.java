package ru.litvak.wishlistservice.integration.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.litvak.wishlistservice.integration.FilesServiceFacade;
import ru.litvak.wishlistservice.integration.request.ClonePictureRequest;

import java.util.UUID;

@Component
public class FilesServiceFacadeImpl implements FilesServiceFacade {


    private final RestTemplate restTemplate;

    public FilesServiceFacadeImpl(@Qualifier(value = "files-service") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void clonePicture(UUID me, String giftId, String newId) {
        restTemplate.postForObject(
                "http://FILES-SERVICE/api/v1/pictures/clone",
                new ClonePictureRequest(me, giftId, newId),
                Void.class
        );
    }
}
