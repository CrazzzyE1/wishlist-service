package ru.litvak.wishlistservice.integration.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;

@Getter
@Setter
public class RelationsDto {
    private PrivacyLevel privacyLevel;
    @JsonProperty("isFriends")
    private boolean isFriends;
}