package ru.litvak.wishlistservice.mapper;

import org.mapstruct.Mapper;
import ru.litvak.wishlistservice.model.dto.PriceDto;
import ru.litvak.wishlistservice.model.entity.Price;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceDto toDto(Price entity);

    List<PriceDto> toListEntity(List<PriceDto> list);

    Price toEntity(PriceDto dto);

    List<PriceDto> toListDto(List<Price> list);
}
