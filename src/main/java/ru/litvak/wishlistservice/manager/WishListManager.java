package ru.litvak.wishlistservice.manager;

import ru.litvak.wishlistservice.model.entity.WishList;
import ru.litvak.wishlistservice.model.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface WishListManager {
    List<WishList> getWishLists(UUID me, UUID userId);

    IdResponse create(WishList wishList);

    WishList getById(UUID me, String id);

    void delete(UUID me, String id);

    IdResponse edit(UUID me, WishList wishList, String wishlistId);
}
