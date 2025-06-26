package ru.litvak.wishlistservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;
import ru.litvak.wishlistservice.model.entity.WishList;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface WishListRepository extends MongoRepository<WishList, String> {

    List<WishList> findByUserId(UUID userId);

    boolean existsWishListByNameAndUserId(String name, UUID userId);

    boolean existsWishListByIdAndUserId(String id, UUID userId);

    List<WishList> findByUserIdAndPrivacyLevelIn(UUID userId, Collection<PrivacyLevel> privacyLevels);
}
