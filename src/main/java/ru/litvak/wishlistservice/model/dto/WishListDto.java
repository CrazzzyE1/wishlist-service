package ru.litvak.wishlistservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.wishlistservice.enumerated.PrivacyLevel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WishListDto {

    private String id;
    @NotEmpty
    private String name;
    private LocalDate eventDate;
    private PrivacyLevel privacyLevel;
    private Boolean isOwner;
    private List<GiftDto> gifts;
}
