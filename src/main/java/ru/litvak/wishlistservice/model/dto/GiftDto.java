package ru.litvak.wishlistservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiftDto {
    private String id;
    private String wishListId;
    @NotEmpty
    private String name;
    private PriceDto price;
    private String link;
    private Boolean isOwner;
}
