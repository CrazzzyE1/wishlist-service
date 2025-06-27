package ru.litvak.wishlistservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GiftDto {
    private String id;
    private String wishListId;
    @NotEmpty
    private String name;
    private BigDecimal price;
    private String link;
    private Boolean isOwner;
}
