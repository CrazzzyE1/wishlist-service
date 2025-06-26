package ru.litvak.wishlistservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.litvak.wishlistservice.model.dto.GiftDto;
import ru.litvak.wishlistservice.model.entity.Gift;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GiftMapper {

    @Mapping(target = "isOwner", ignore = true)
    GiftDto toDto(Gift entity);

    List<GiftDto> toListEntity(List<GiftDto> list);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletionReason", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "privacyLevel", ignore = true)
    Gift toEntity(GiftDto dto);

    List<GiftDto> toListDto(List<Gift> list);
}
