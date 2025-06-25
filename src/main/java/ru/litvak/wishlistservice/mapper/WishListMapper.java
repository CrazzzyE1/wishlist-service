package ru.litvak.wishlistservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.litvak.wishlistservice.model.dto.WishListDto;
import ru.litvak.wishlistservice.model.entity.WishList;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishListMapper {

    @Mapping(target = "isOwner", ignore = true)
    WishListDto toDto(WishList entity);

    List<WishListDto> toListDto(List<WishList> list);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletionReason", ignore = true)
    WishList toEntity(WishListDto dto);
}
