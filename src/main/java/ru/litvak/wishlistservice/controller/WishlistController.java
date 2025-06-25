package ru.litvak.wishlistservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvak.wishlistservice.model.dto.WishListDto;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.service.WishListService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlists")
public class WishlistController {

    private final WishListService wishListService;

    @GetMapping("/me")
    public List<WishListDto> getOwnerWishLists(@RequestHeader(value = "Authorization") String authHeader) {
        return wishListService.getMyWishLists(authHeader);
    }

    @GetMapping("/user/{userId}")
    public List<WishListDto> getWishLists(@RequestHeader(value = "Authorization") String authHeader,
                                          @PathVariable UUID userId) {
        return wishListService.getWishLists(authHeader, userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public IdResponse createWishList(@RequestHeader(value = "Authorization") String authHeader,
                                     @RequestBody @Valid WishListDto wishListDto) {
        return wishListService.createWishList(authHeader, wishListDto);
    }

    @GetMapping("/{wishlistId}")
    public WishListDto getWishListById(@RequestHeader(value = "Authorization") String authHeader,
                                             @PathVariable String wishlistId) {
        return wishListService.getWishListById(authHeader, wishlistId);
    }
}
