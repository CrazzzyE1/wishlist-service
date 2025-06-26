package ru.litvak.wishlistservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.service.GiftService;

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
}
