package ru.litvak.wishlistservice.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.litvak.wishlistservice.enumerated.WorldCurrency;

import java.math.BigDecimal;

@Getter
@Setter
public class Price {
    private BigDecimal amount;
    private WorldCurrency currency;
}
