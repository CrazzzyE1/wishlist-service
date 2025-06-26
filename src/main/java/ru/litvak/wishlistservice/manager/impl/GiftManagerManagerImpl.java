package ru.litvak.wishlistservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;
import ru.litvak.wishlistservice.integration.UserServiceFacade;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.model.dto.RelationsDto;
import ru.litvak.wishlistservice.model.dto.WishListId;
import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.model.entity.WishList;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.repository.GiftRepository;
import ru.litvak.wishlistservice.repository.WishListRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.litvak.wishlistservice.enumerated.PrivacyLevel.FRIENDS_ONLY;
import static ru.litvak.wishlistservice.enumerated.PrivacyLevel.PUBLIC;

@Component
@RequiredArgsConstructor
public class GiftManagerManagerImpl implements GiftManager {

    private final GiftRepository giftRepository;
    private final WishListRepository wishListRepository;
    private final UserServiceFacade userServiceFacade;

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

    @Override
    public void delete(UUID me, String id) {
        giftRepository.deleteByIdAndUserId(id, me);
    }

    @Override
    public List<Gift> getGifts(UUID me, UUID userId) {
        if (userId.equals(me)) {
            return giftRepository.findByUserId(userId);
        }

        RelationsDto relationsDto = userServiceFacade.getRelations(me, userId);
        PrivacyLevel userPrivacyLevel = relationsDto.getPrivacyLevel();
        boolean friends = relationsDto.isFriends();
        if (PUBLIC.equals(userPrivacyLevel)) {
            List<PrivacyLevel> searchPrivacyLevels = friends ? List.of(PUBLIC, FRIENDS_ONLY) : List.of(PUBLIC);
            List<String> ids = wishListRepository.findIdsByUserIdAndPrivacyLevelIn(userId, searchPrivacyLevels).stream()
                    .map(WishListId::getId)
                    .toList();
            return giftRepository.findAllByWishListIdIn(ids);
        }

        if (FRIENDS_ONLY.equals(userPrivacyLevel) && friends) {
            List<String> ids = wishListRepository.findIdsByUserIdAndPrivacyLevelIn(userId, List.of(PUBLIC, FRIENDS_ONLY)).stream()
                    .map(WishListId::getId)
                    .toList();
            return giftRepository.findAllByWishListIdIn(ids);
        }
        return List.of();
    }
}
