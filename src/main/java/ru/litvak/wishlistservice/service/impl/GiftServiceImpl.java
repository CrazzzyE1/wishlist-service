package ru.litvak.wishlistservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.mapper.GiftMapper;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.dto.GiftInfoDto;
import ru.litvak.wishlistservice.model.dto.GiftsCountDto;
import ru.litvak.wishlistservice.model.request.AddGiftRequest;
import ru.litvak.wishlistservice.model.response.IdResponse;
import ru.litvak.wishlistservice.service.GiftService;
import ru.litvak.wishlistservice.util.JwtTokenMapper;

import java.util.List;
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

    @Override
    public void deleteGift(String authHeader, String id) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        giftManager.delete(me, id);
    }

    @Override
    public List<GiftDto> getOwnerGifts(String authHeader, Boolean withList) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return giftMapper.toListDto(giftManager.getGifts(me, me, withList));
    }

    @Override
    public List<GiftDto> getGifts(String authHeader, UUID userId, Boolean withList) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return giftMapper.toListDto(giftManager.getGifts(me, userId, withList));
    }

    @Override
    public IdResponse addGift(String authHeader, AddGiftRequest request) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return giftManager.add(me, request.getGiftId());
    }

    @Override
    public GiftInfoDto getGiftInfo(String id) {
        return giftManager.getInfo(id);
    }

    @Override
    public GiftsCountDto getGiftsCount(String authHeader, UUID userId) {
        return giftManager.getCount(userId);
    }

    @Override
    public IdResponse editGift(String authHeader, GiftDto giftDto, String giftId) {
        UUID me = JwtTokenMapper.parseUserId(authHeader);
        return giftManager.edit(me, giftMapper.toEntity(giftDto), giftId);
    }
}
