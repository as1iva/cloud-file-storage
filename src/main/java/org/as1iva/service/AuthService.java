package org.as1iva.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.as1iva.dto.request.UserLoginRequestDto;
import org.as1iva.dto.request.UserRegistrationRequestDto;
import org.as1iva.dto.response.UserLoginResponseDto;
import org.as1iva.dto.response.UserRegistrationResponseDto;
import org.as1iva.entity.User;
import org.as1iva.mapper.UserLoginMapper;
import org.as1iva.mapper.UserRegistrationMapper;
import org.as1iva.repository.UserRepository;
import org.as1iva.security.SecurityUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UserRegistrationResponseDto signUp(UserRegistrationRequestDto userRegistrationRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDto.password());

        User user = User.builder()
                .username(userRegistrationRequestDto.username())
                .password(encodedPassword)
                .build();

        return UserRegistrationMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public UserLoginResponseDto signIn(UserLoginRequestDto userLoginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.username(),
                        userLoginRequestDto.password()
                )
        );

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getSession();

        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        SecurityUserDetails principal = (SecurityUserDetails) authentication.getPrincipal();

        User userEntity = User.builder()
                .username(principal.getUsername())
                .build();

        return UserLoginMapper.INSTANCE.toDto(userEntity);
    }
}
