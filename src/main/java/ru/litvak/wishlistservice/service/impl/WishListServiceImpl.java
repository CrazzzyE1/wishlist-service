package ru.litvak.wishlistservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.manager.WishListManager;
import ru.litvak.wishlistservice.mapper.GiftMapper;
import ru.litvak.wishlistservice.mapper.WishListMapper;
import ru.litvak.wishlistservice.model.dto.WishListDto;
import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.model.entity.WishList;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.service.WishListService;
import ru.litvak.wishlistservice.util.JwtTokenMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListManager wishListManager;
    private final GiftManager giftManager;
    private final WishListMapper wishListMapper;
    private final GiftMapper giftMapper;

    @Override
    public List<WishListDto> getOwnerWishLists(String authHeader) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        List<WishListDto> dtoList = wishListMapper.toListDto(wishListManager.getWishLists(me, me));
        dtoList.forEach(w -> w.setIsOwner(true));
        return dtoList;
    }

    @Override
    public List<WishListDto> getWishLists(String authHeader, UUID userId) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return wishListMapper.toListDto(wishListManager.getWishLists(me, userId));
    }

    @Override
    public WishListDto getWishListById(String authHeader, String wishlistId) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        WishList wishList = wishListManager.getById(me, wishlistId);
        WishListDto dto = wishListMapper.toDto(wishList);
        if (dto == null) {
            return null;
        }
        List<Gift> gifts = giftManager.getAllByWishListId(dto.getId());
        dto.setGifts(giftMapper.toListDto(gifts));
        return dto;
    }

    @Override
    public void deleteWishListById(String authHeader, String wishlistId) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        wishListManager.delete(me, wishlistId);
    }

    @Override
    public IdResponse editWishList(String authHeader, WishListDto dto, String wishlistId) {
        WishList wishList = wishListMapper.toEntity(dto);
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return wishListManager.edit(me, wishList, wishlistId);
    }

    @Override
    public IdResponse createWishList(String authHeader, WishListDto dto) {
        WishList wishList = wishListMapper.toEntity(dto);
        wishList.setUserId(JwtTokenMapper.parseUserId(authHeader));
        return wishListManager.create(wishList);
    }
}
