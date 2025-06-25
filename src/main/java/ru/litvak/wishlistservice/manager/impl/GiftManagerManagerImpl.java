package ru.litvak.wishlistservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.wishlistservice.manager.GiftManager;
import ru.litvak.wishlistservice.model.entity.Gift;
import ru.litvak.wishlistservice.repository.GiftRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GiftManagerManagerImpl implements GiftManager {

    private final GiftRepository giftRepository;

    @Override
    public List<Gift> getAllByWishListId(String id) {
        return giftRepository.findAllByWishListId(id);
    }
}
