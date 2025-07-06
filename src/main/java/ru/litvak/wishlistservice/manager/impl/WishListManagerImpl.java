package ru.litvak.wishlistservice.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;
import ru.litvak.wishlistservice.exception.NotFoundException;
import ru.litvak.wishlistservice.integration.UserServiceFacade;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.manager.WishListManager;
import ru.litvak.wishlistservice.integration.response.RelationsDto;
import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.model.entity.WishList;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.repository.GiftRepository;
import ru.litvak.wishlistservice.repository.WishListRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.litvak.wishlistservice.enumerated.DeleteReason.USER_REQUEST;
import static ru.litvak.wishlistservice.enumerated.PrivacyLevel.FRIENDS_ONLY;
import static ru.litvak.wishlistservice.enumerated.PrivacyLevel.PUBLIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class WishListManagerImpl implements WishListManager {

    private final WishListRepository wishListRepository;
    private final GiftRepository giftRepository;
    private final GiftManager giftManager;
    private final UserServiceFacade userServiceFacade;

    @Override
    public List<WishList> getWishLists(UUID me, UUID userId) {
        if (userId.equals(me)) {
            return wishListRepository.findByUserIdAndIsDeletedFalse(userId);
        }

        RelationsDto relationsDto = userServiceFacade.getRelations(me, userId);
        PrivacyLevel userPrivacyLevel = relationsDto.getPrivacyLevel();
        boolean friends = relationsDto.isFriends();
        if (PUBLIC.equals(userPrivacyLevel)) {
            List<PrivacyLevel> searchPrivacyLevels = friends ? List.of(PUBLIC, FRIENDS_ONLY) : List.of(PUBLIC);
            return wishListRepository.findByUserIdAndPrivacyLevelInAndIsDeletedFalse(userId, searchPrivacyLevels);
        }

        if (FRIENDS_ONLY.equals(userPrivacyLevel) && friends) {
            return wishListRepository.findByUserIdAndPrivacyLevelInAndIsDeletedFalse(userId, List.of(PUBLIC, FRIENDS_ONLY));
        }
        return List.of();
    }

    @Override
    public IdResponse create(WishList wishList) {
        String name = wishList.getName();
        if (wishListRepository.existsWishListByNameAndUserIdAndIsDeletedFalse(name, wishList.getUserId())) {
            throw new NotFoundException("WishList with name %s already exists".formatted(name));
        }
        WishList saved = wishListRepository.save(wishList);
        return new IdResponse(saved.getId());
    }

    @Override
    public WishList getById(UUID me, String id) {
        WishList wishList = wishListRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("WishList with id %s not found.".formatted(id)));
        UUID userId = wishList.getUserId();

        if (me.equals(userId)) {
            return wishList;
        }

        RelationsDto relationsDto = userServiceFacade.getRelations(me, userId);
        PrivacyLevel userPrivacyLevel = relationsDto.getPrivacyLevel();
        boolean friends = relationsDto.isFriends();

        if (PUBLIC.equals(userPrivacyLevel)) {
            List<PrivacyLevel> searchPrivacyLevels = friends ? List.of(PUBLIC, FRIENDS_ONLY) : List.of(PUBLIC);
            return searchPrivacyLevels.contains(wishList.getPrivacyLevel()) ? wishList : null;
        }

        if (FRIENDS_ONLY.equals(userPrivacyLevel) && friends) {
            return List.of(PUBLIC, FRIENDS_ONLY).contains(wishList.getPrivacyLevel()) ? wishList : null;
        }

        return null;
    }

    @Override
    public void delete(UUID me, String id) {
        Optional<WishList> optional = wishListRepository.findByIdAndUserIdAndIsDeletedFalse(id, me);
        if (optional.isEmpty()) {
            log.warn("An attempt to delete someone else's wishlist with id: {}, userId: {}", id, me);
            return;
        }
        WishList wishList = optional.get();
        List<Gift> gifts = giftManager.getAllByWishListId(wishList.getId());

        gifts.forEach(gift -> {
            gift.setIsDeleted(true);
            gift.setDeletedAt(Instant.now());
            gift.setDeletionReason(USER_REQUEST);
            giftRepository.save(gift);
        });

        wishList.setIsDeleted(true);
        wishList.setDeletedAt(Instant.now());
        wishList.setDeletionReason(USER_REQUEST);
        wishListRepository.save(wishList);
    }
}
