package org.as1iva.mapper;

import org.as1iva.dto.request.UserRegistrationRequestDto;
import org.as1iva.dto.response.UserRegistrationResponseDto;
import org.as1iva.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserRegistrationResponseDto toDto(User user);

    User toEntity(UserRegistrationRequestDto userRegistrationRequestDto);
}
