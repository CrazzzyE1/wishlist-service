package ru.litvak.wishlistservice.manager;

import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.model.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface GiftManager {

    List<Gift> getAllByWishListId(String id);

    IdResponse create(UUID me, Gift gift);

    void delete(UUID me, String id);

    List<Gift> getGifts(UUID me, UUID userId, boolean withList);

    void add(UUID me, String id);
}
