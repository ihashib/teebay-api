package com.teebay.teebayapi.service.mapper;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.service.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}
