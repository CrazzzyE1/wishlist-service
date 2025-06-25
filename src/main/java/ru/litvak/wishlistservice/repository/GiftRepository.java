package ru.litvak.wishlistservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.litvak.wishlistservice.model.entity.Gift;

import java.util.List;

@Repository
public interface GiftRepository extends MongoRepository<Gift, String> {

    List<Gift> findAllByWishListId(final String wishListId);
}
