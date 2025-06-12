package com.teebay.teebayapi.service.mapper;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toDto(User user);
}
