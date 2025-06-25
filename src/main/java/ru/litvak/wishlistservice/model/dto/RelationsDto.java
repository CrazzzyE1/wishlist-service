package ru.litvak.wishlistservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;

@Getter
@Setter
@ToString
public class RelationsDto {
    private PrivacyLevel privacyLevel;
    private boolean isFriend;
}
