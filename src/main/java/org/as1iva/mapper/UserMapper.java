package org.as1iva.mapper;

import org.as1iva.dto.request.UserRequestDto;
import org.as1iva.dto.response.UserResponseDto;
import org.as1iva.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto userRequestDto);
}
