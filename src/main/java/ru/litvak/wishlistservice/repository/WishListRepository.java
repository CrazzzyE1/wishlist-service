package ru.litvak.wishlistservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;
import ru.litvak.wishlistservice.model.dto.WishListId;
import ru.litvak.wishlistservice.model.entity.WishList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishListRepository extends MongoRepository<WishList, String> {

    List<WishList> findByUserIdAndIsDeletedFalse(UUID userId);

    boolean existsWishListByNameAndUserIdAndIsDeletedFalse(String name, UUID userId);

    boolean existsWishListByIdAndUserIdAndIsDeletedFalse(String id, UUID userId);

    Optional<WishList> findByIdAndUserIdAndIsDeletedFalse(String id, UUID userId);

    List<WishList> findByUserIdAndPrivacyLevelInAndIsDeletedFalse(UUID userId, Collection<PrivacyLevel> privacyLevels);

    @Query(value = "{ 'userId': ?0, 'privacyLevel': { $in: ?1 } }", fields = "{ '_id': 1 }")
    List<WishListId> findIdsByUserIdAndPrivacyLevelInAndIsDeletedFalse(UUID userId, Collection<PrivacyLevel> privacyLevels);

    Optional<WishList> findByIdAndIsDeletedFalse(String id);
}
