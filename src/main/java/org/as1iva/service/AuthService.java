package org.as1iva.service;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.request.UserRegistrationRequestDto;
import org.as1iva.dto.response.UserRegistrationResponseDto;
import org.as1iva.entity.User;
import org.as1iva.mapper.UserMapper;
import org.as1iva.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationResponseDto signUp(UserRegistrationRequestDto userRegistrationRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDto.password());

        User user = User.builder()
                .username(userRegistrationRequestDto.username())
                .password(encodedPassword)
                .build();

        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }
}
