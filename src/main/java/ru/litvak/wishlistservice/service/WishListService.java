package ru.litvak.wishlistservice.service;

import jakarta.validation.Valid;
import ru.litvak.wishlistservice.model.dto.WishListDto;
import ru.litvak.wishlistservice.model.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface WishListService {
    List<WishListDto> getOwnerWishLists(String authHeader);

    IdResponse createWishList(String authHeader, WishListDto dto);

    List<WishListDto> getWishLists(String authHeader, UUID userId);

    WishListDto getWishListById(String authHeader, String wishlistId);

    void deleteWishListById(String authHeader, String wishlistId);

    IdResponse editWishList(String authHeader, @Valid WishListDto wishListDto, String wishlistId);
}
