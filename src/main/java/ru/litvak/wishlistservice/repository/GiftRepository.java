package ru.litvak.wishlistservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.litvak.wishlistservice.model.entity.Gift;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GiftRepository extends MongoRepository<Gift, String> {

    List<Gift> findAllByWishListIdAndIsDeletedFalse(final String wishListId);

    List<Gift> findAllByWishListIdInAndIsDeletedFalse(List<String> wishListId);

    List<Gift> findByUserIdAndWishListIdNotNullAndIsDeletedFalse(UUID userId);

    List<Gift> findByUserIdAndWishListIdIsNullAndIsDeletedFalse(UUID userId);

    Optional<Gift> findByIdAndUserIdAndIsDeletedFalse(String id, UUID me);

    Optional<Gift> findByIdAndIsDeletedFalse(String id);
}
