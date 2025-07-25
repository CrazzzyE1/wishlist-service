package ru.litvak.wishlistservice.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGiftRequest {

    @NotEmpty
    private String giftId;
}
