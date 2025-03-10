package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.request.UserLoginRequestDto;
import org.as1iva.dto.request.UserRegistrationRequestDto;
import org.as1iva.dto.response.UserLoginResponseDto;
import org.as1iva.dto.response.UserRegistrationResponseDto;
import org.as1iva.service.AuthService;
import org.as1iva.util.ValidationUtil;
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

        ValidationUtil.checkUsername(user.username());

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(user));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserLoginResponseDto> signIn(@RequestBody UserLoginRequestDto user) {

        ValidationUtil.checkUsername(user.username());

        return ResponseEntity.ok(authService.signIn(user));
    }
}
