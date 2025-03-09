package org.as1iva.mapper;

import org.as1iva.dto.request.UserLoginRequestDto;
import org.as1iva.dto.response.UserLoginResponseDto;
import org.as1iva.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserLoginMapper {
    UserLoginMapper INSTANCE = Mappers.getMapper(UserLoginMapper.class);

    UserLoginResponseDto toDto(User user);

    User toEntity(UserLoginRequestDto userLoginRequestDto);
}
