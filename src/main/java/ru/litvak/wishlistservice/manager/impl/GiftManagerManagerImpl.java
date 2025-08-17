package ru.litvak.wishlistservice.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;
import ru.litvak.wishlistservice.exception.NotFoundException;
import ru.litvak.wishlistservice.integration.UserServiceFacade;
import ru.litvak.wishlistservice.integration.response.RelationsDto;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.model.dto.GiftInfoDto;
import ru.litvak.wishlistservice.model.dto.GiftsCountDto;
import ru.litvak.wishlistservice.model.dto.WishListId;
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
public class GiftManagerManagerImpl implements GiftManager {

    private final GiftRepository giftRepository;
    private final WishListRepository wishListRepository;
    private final UserServiceFacade userServiceFacade;

    @Override
    public List<Gift> getAllByWishListId(String id) {
        return giftRepository.findAllByWishListIdAndIsDeletedFalse(id);
    }

    @Override
    public IdResponse create(UUID me, Gift gift) {
        String wishListId = gift.getWishListId();
        if (wishListId != null && !wishListRepository.existsWishListByIdAndUserIdAndIsDeletedFalse(wishListId, me)) {
            throw new NotFoundException("WishList id %s does not exist".formatted(wishListId));
        }
        gift.setUserId(me);
        Gift saved = giftRepository.save(gift);
        return new IdResponse(saved.getId());
    }

    @Override
    @Transactional
    public void delete(UUID me, String id) {
        Optional<Gift> optionalGift = giftRepository.findByIdAndUserIdAndIsDeletedFalse(id, me);
        if (optionalGift.isEmpty()) {
            log.warn("An attempt to delete someone else's gift with id: {}, userId: {}", id, me);
            return;
        }
        Gift gift = optionalGift.get();
        gift.setIsDeleted(true);
        gift.setDeletedAt(Instant.now());
        gift.setDeletionReason(USER_REQUEST);
        giftRepository.save(gift);
    }

    @Override
    public List<Gift> getGifts(UUID me, UUID userId, boolean withList) {
        if (userId.equals(me)) {
            if (withList) {
                return giftRepository.findByUserIdAndWishListIdNotNullAndIsDeletedFalse(userId);
            }
            return giftRepository.findByUserIdAndWishListIdIsNullAndIsDeletedFalse(userId);
        }

        RelationsDto relationsDto = userServiceFacade.getRelations(me, userId);
        PrivacyLevel userPrivacyLevel = relationsDto.getPrivacyLevel();
        boolean friends = relationsDto.isFriends();
        if (PUBLIC.equals(userPrivacyLevel)) {
            List<PrivacyLevel> searchPrivacyLevels = friends ? List.of(PUBLIC, FRIENDS_ONLY) : List.of(PUBLIC);
            List<String> ids = wishListRepository.findIdsByUserIdAndPrivacyLevelInAndIsDeletedFalse(userId, searchPrivacyLevels).stream()
                    .map(WishListId::getId)
                    .toList();
            if (withList) {
                return giftRepository.findAllByWishListIdInAndIsDeletedFalse(ids);
            }
            return giftRepository.findByUserIdAndWishListIdIsNullAndIsDeletedFalse(userId);
        }

        if (FRIENDS_ONLY.equals(userPrivacyLevel) && friends) {
            List<String> ids = wishListRepository.findIdsByUserIdAndPrivacyLevelInAndIsDeletedFalse(userId, List.of(PUBLIC, FRIENDS_ONLY)).stream()
                    .map(WishListId::getId)
                    .toList();
            if (withList) {
                return giftRepository.findAllByWishListIdInAndIsDeletedFalse(ids);
            }
            return giftRepository.findByUserIdAndWishListIdIsNullAndIsDeletedFalse(userId);
        }
        return List.of();
    }

    @Override
    public IdResponse add(UUID me, String id) {
        Gift gift = giftRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Gift with id %s not found.".formatted(id)));
        UUID userId = gift.getUserId();
        String wishListId = gift.getWishListId();
        RelationsDto relationsDto = userServiceFacade.getRelations(me, userId);
        PrivacyLevel userPrivacyLevel = relationsDto.getPrivacyLevel();
        boolean friends = relationsDto.isFriends();

        if (wishListId == null
                && (PUBLIC.equals(userPrivacyLevel) || friends && FRIENDS_ONLY.equals(userPrivacyLevel))) {
            return add(me, gift);
        }

        if (wishListId != null) {
            WishList wishList = wishListRepository.findByIdAndIsDeletedFalse(wishListId)
                    .orElseThrow(() -> new NotFoundException("WishList with id %s not found.".formatted(wishListId)));

            if (PUBLIC.equals(userPrivacyLevel)) {
                List<PrivacyLevel> searchPrivacyLevels = friends ? List.of(PUBLIC, FRIENDS_ONLY) : List.of(PUBLIC);
                if (searchPrivacyLevels.contains(wishList.getPrivacyLevel())) {
                    return add(me, gift);
                }
            }

            if (FRIENDS_ONLY.equals(userPrivacyLevel) && friends) {
                if (List.of(PUBLIC, FRIENDS_ONLY).contains(wishList.getPrivacyLevel())) {
                    return add(me, gift);
                }
            }
        }
        return null;
    }

    @Override
    public GiftInfoDto getInfo(String id) {
        Gift gift = giftRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Gift with id %s not found.".formatted(id)));
        Optional<WishList> listOptional = wishListRepository.findByIdAndIsDeletedFalse(gift.getWishListId());
        return new GiftInfoDto(gift.getUserId(), listOptional.map(WishList::getPrivacyLevel).orElse(null));
    }

    @Override
    public GiftsCountDto getCount(UUID userId) {
        return new GiftsCountDto(giftRepository.countByUserIdAndIsDeletedFalse(userId));
    }

    @Override
    @Transactional
    public IdResponse edit(UUID me, Gift info, String id) {
        Gift gift = giftRepository.findByIdAndIsDeletedFalseAndUserId(id, me)
                .orElseThrow(() -> new NotFoundException("Gift with id %s not found.".formatted(id)));
        gift.setName(info.getName());
        gift.setPrice(gift.getPrice());
        gift.setWishListId(info.getWishListId());
        gift.setLink(info.getLink());
        gift.setDescription(info.getDescription());
        giftRepository.save(gift);
        return new IdResponse(gift.getId());
    }

    private IdResponse add(UUID me, Gift gift) {
        Gift toSave = new Gift();
        toSave.setName(gift.getName());
        toSave.setPrice(gift.getPrice());
        toSave.setLink(gift.getLink());
        toSave.setUserId(me);
        return new IdResponse(giftRepository.save(toSave).getId());
    }
}
