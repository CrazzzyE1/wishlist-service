package ru.litvak.wishlistservice.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.litvak.wishlistservice.enumerated.DeleteReason;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
@Document(collection = "gifts")
public class Gift {
    @Id
    private String id;
    private String wishListId;
    private String name;
    private BigDecimal price;
    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    private Boolean isDeleted = false;
    private Instant deletedAt;
    private DeleteReason deletionReason;
}
