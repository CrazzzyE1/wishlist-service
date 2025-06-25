package ru.litvak.wishlistservice.integration;

import ru.litvak.wishlistservice.model.dto.RelationsDto;

import java.util.UUID;

public interface UserServiceFacade {

    RelationsDto getRelations(UUID me, UUID userId);
}
