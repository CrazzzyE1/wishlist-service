package ru.litvak.wishlistservice.service;

import jakarta.validation.Valid;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface GiftService {
    IdResponse createGift(String authHeader, @Valid GiftDto giftDto);

    void deleteGift(String authHeader, String id);

    List<GiftDto> getOwnerGifts(String authHeader);

    List<GiftDto> getGifts(String authHeader, UUID userId);
}
