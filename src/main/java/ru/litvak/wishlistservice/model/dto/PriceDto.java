package ru.litvak.wishlistservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.litvak.wishlistservice.enumerated.WorldCurrency;

import java.math.BigDecimal;

@Getter
@Setter
public class PriceDto {
    private BigDecimal amount;
    private WorldCurrency currency;
}
