package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.request.UserRegistrationRequestDto;
import org.as1iva.dto.response.UserRegistrationResponseDto;
import org.as1iva.entity.User;
import org.as1iva.exception.InvalidDataException;
import org.as1iva.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserRegistrationResponseDto> signUp(@RequestBody UserRegistrationRequestDto user) {

        if (!user.password().equals(user.confirmPassword())) {
            throw new InvalidDataException("Passwords doesn't match");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(user));
    }
}
