package ru.litvak.wishlistservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GiftInfoDto {
    private UUID userId;
    private PrivacyLevel wishListPrivacyLevel;
}
