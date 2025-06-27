package ru.litvak.wishlistservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.litvak.wishlistservice.model.entity.Gift;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GiftRepository extends MongoRepository<Gift, String> {

    List<Gift> findAllByWishListId(final String wishListId);

    List<Gift> findAllByWishListIdIn(List<String> wishListId);

    void deleteByIdAndUserId(final String id, final UUID userId);

    List<Gift> findByUserIdAndWishListIdNotNull(UUID userId);

    List<Gift> findByUserIdAndWishListIdIsNull(UUID userId);

    Optional<Gift> findByIdAndUserId(String id, UUID me);
}
