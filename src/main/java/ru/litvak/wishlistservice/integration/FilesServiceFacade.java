package ru.litvak.wishlistservice.integration;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public interface FilesServiceFacade {

    void clonePicture(UUID me, @NotEmpty String giftId, String id);
}
