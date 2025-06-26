package ru.litvak.wishlistservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.service.GiftService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gifts")
public class GiftController {

    private final GiftService giftService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public IdResponse createGift(@RequestHeader(value = "Authorization") String authHeader,
                                 @RequestBody @Valid GiftDto giftDto) {
        return giftService.createGift(authHeader, giftDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteGift(@RequestHeader(value = "Authorization") String authHeader,
                           @PathVariable String id) {
        giftService.deleteGift(authHeader, id);
    }

    @GetMapping(value = "/me")
    public List<GiftDto> getOwnerGifts(@RequestHeader(value = "Authorization") String authHeader) {
        return giftService.getOwnerGifts(authHeader);
    }

    @GetMapping(value = "/user/{userId}")
    public List<GiftDto> getGifts(@RequestHeader(value = "Authorization") String authHeader,
                                  @PathVariable UUID userId) {
        return giftService.getGifts(authHeader, userId);
    }
}
