package ru.litvak.wishlistservice.integration;

import ru.litvak.wishlistservice.integration.response.RelationsDto;

import java.util.UUID;

public interface UserServiceFacade {

    RelationsDto getRelations(UUID me, UUID userId);
}
