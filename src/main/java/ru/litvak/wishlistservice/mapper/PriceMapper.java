package ru.litvak.wishlistservice.mapper;

import org.mapstruct.Mapper;
import ru.litvak.wishlistservice.model.dto.PriceDto;
import ru.litvak.wishlistservice.model.entity.Price;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceDto toDto(Price entity);

    Price toEntity(PriceDto dto);

}
