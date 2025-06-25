package ru.litvak.wishlistservice.manager;

import ru.litvak.wishlistservice.model.entity.Gift;

import java.util.List;

public interface GiftManager {

    List<Gift> getAllByWishListId(String id);
}
