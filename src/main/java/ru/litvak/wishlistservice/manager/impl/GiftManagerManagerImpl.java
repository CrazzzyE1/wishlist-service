package ru.litvak.wishlistservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.model.entity.WishList;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.repository.GiftRepository;
import ru.litvak.wishlistservice.repository.WishListRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GiftManagerManagerImpl implements GiftManager {

    private final GiftRepository giftRepository;
    private final WishListRepository wishListRepository;

    @Override
    public List<Gift> getAllByWishListId(String id) {
        return giftRepository.findAllByWishListId(id);
    }

    @Override
    public IdResponse create(UUID me, Gift gift) {
        String wishListId = gift.getWishListId();
        if (wishListId != null) {
            if (!wishListRepository.existsWishListByIdAndUserId(wishListId, me)) {
                throw new RuntimeException("WishList id %s does not exist".formatted(wishListId));
            }
        } else {
            WishList wishList = new WishList();
            wishList.setUserId(me);
            // FIXME 26.06.2025:13:23: Изменить логику дефолтного списка
            wishList.setName("WishList (%s)".formatted(LocalDateTime.now()));
            WishList emptyListSaved = wishListRepository.save(wishList);
            gift.setWishListId(emptyListSaved.getId());
        }
        gift.setUserId(me);
        Gift saved = giftRepository.save(gift);
        return new IdResponse(saved.getId());
    }
}
