package ru.litvak.wishlistservice.manager;

import ru.litvak.wishlistservice.model.dto.GiftInfoDto;
import ru.litvak.wishlistservice.model.dto.GiftsCountDto;
import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.model.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface GiftManager {

    List<Gift> getAllByWishListId(String id);

    IdResponse create(UUID me, Gift gift);

    void delete(UUID me, String id);

    List<Gift> getGifts(UUID me, UUID userId, boolean withList);

    IdResponse add(UUID me, String id);

    GiftInfoDto getInfo(String id);

    GiftsCountDto getCount(UUID userId);

    IdResponse edit(UUID me, Gift giftDto, String giftId);
}
