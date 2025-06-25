package ru.litvak.wishlistservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;
import ru.litvak.wishlistservice.integration.UserServiceFacade;
import ru.litvak.wishlistservice.manager.WishListManager;
import ru.litvak.wishlistservice.model.dto.RelationsDto;
import ru.litvak.wishlistservice.model.entity.WishList;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.repository.WishListRepository;

import java.util.List;
import java.util.UUID;

import static ru.litvak.wishlistservice.enumerated.PrivacyLevel.FRIENDS_ONLY;
import static ru.litvak.wishlistservice.enumerated.PrivacyLevel.PUBLIC;

@Component
@RequiredArgsConstructor
public class WishListManagerImpl implements WishListManager {

    private final WishListRepository wishListRepository;
    private final UserServiceFacade userServiceFacade;

    @Override
    public List<WishList> getWishLists(UUID me, UUID userId) {
        if (userId.equals(me)) {
            return wishListRepository.findByUserId(userId);
        }

        RelationsDto relationsDto = userServiceFacade.getRelations(me, userId);
        PrivacyLevel userPrivacyLevel = relationsDto.getPrivacyLevel();
        boolean friends = relationsDto.isFriends();
        if (PUBLIC.equals(userPrivacyLevel)) {
            List<PrivacyLevel> searchPrivacyLevels = friends ? List.of(PUBLIC, FRIENDS_ONLY) : List.of(PUBLIC);
            return wishListRepository.findByUserIdAndPrivacyLevelIn(userId, searchPrivacyLevels);
        }

        if (FRIENDS_ONLY.equals(userPrivacyLevel) && friends) {
            return wishListRepository.findByUserIdAndPrivacyLevelIn(userId, List.of(PUBLIC, FRIENDS_ONLY));
        }
        return List.of();
    }

    @Override
    public IdResponse create(WishList wishList) {
        String name = wishList.getName();
        if (wishListRepository.existsWishListByNameAndUserId(name, wishList.getUserId())) {
            throw new RuntimeException("WishList with name %s already exists".formatted(name));
        }
        WishList saved = wishListRepository.save(wishList);
        return new IdResponse(saved.getId());
    }

    @Override
    public WishList getById(UUID me, String id) {
        WishList wishList = wishListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WishList with id %s not found.".formatted(id)));
        UUID userId = wishList.getUserId();
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
}
