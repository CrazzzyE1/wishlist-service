package ru.litvak.wishlistservice.integration.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.litvak.wishlistservice.integration.UserServiceFacade;
import ru.litvak.wishlistservice.model.dto.RelationRequest;
import ru.litvak.wishlistservice.model.dto.RelationsDto;

import java.util.UUID;

@Component
public class UserServiceFacadeImpl implements UserServiceFacade {


    private final RestTemplate restTemplate;

    public UserServiceFacadeImpl(@Qualifier(value = "user-service") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RelationsDto getRelations(UUID me, UUID userId) {
        return restTemplate.postForObject(
                "http://USER-SERVICE/api/v1/profiles/relations",
                new RelationRequest(me, userId),
                RelationsDto.class
        );
    }
}
