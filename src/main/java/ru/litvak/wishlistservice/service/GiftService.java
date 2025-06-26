package ru.litvak.wishlistservice.service;

import jakarta.validation.Valid;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.response.IdResponse;

public interface GiftService {
    IdResponse createGift(String authHeader, @Valid GiftDto giftDto);
}
