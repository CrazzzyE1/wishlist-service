package ru.litvak.wishlistservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.mapper.GiftMapper;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.service.GiftService;
import ru.litvak.wishlistservice.util.JwtTokenMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {

    private final GiftManager giftManager;
    private final GiftMapper giftMapper;

    @Override
    public IdResponse createGift(String authHeader, GiftDto giftDto) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return giftManager.create(me, giftMapper.toEntity(giftDto));
    }
}
