package ru.litvak.wishlistservice.service;

import jakarta.validation.Valid;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.dto.GiftInfoDto;
import ru.litvak.wishlistservice.model.dto.GiftsCountDto;
import ru.litvak.wishlistservice.model.request.AddGiftRequest;
import ru.litvak.wishlistservice.model.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface GiftService {
    IdResponse createGift(String authHeader, @Valid GiftDto giftDto);

    void deleteGift(String authHeader, String id);

    List<GiftDto> getOwnerGifts(String authHeader, Boolean withList);

    List<GiftDto> getGifts(String authHeader, UUID userId, Boolean withList);

    IdResponse addGift(String authHeader, @Valid AddGiftRequest request);

    GiftInfoDto getGiftInfo(String id);

    GiftsCountDto getGiftsCount(String authHeader, UUID userId);

    IdResponse editGift(String authHeader, @Valid GiftDto giftDto, String giftId);
}
