package ru.litvak.wishlistservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.wishlistservice.manager.WishListManager;
import ru.litvak.wishlistservice.mapper.WishListMapper;
import ru.litvak.wishlistservice.model.dto.WishListDto;
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
    private final WishListMapper wishListMapper;

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
        return wishListMapper.toDto(wishListManager.get(me, wishlistId));
    }

    @Override
    public IdResponse createWishList(String authHeader, WishListDto dto) {
        WishList wishList = wishListMapper.toEntity(dto);
        wishList.setUserId(JwtTokenMapper.parseUserId(authHeader));
        return wishListManager.create(wishList);
    }
}
