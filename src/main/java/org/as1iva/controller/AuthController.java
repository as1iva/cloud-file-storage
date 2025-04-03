package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.docs.auth.SignInUserDocs;
import org.as1iva.dto.request.UserRequestDto;
import org.as1iva.dto.response.UserResponseDto;
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
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto user) {

        ValidationUtil.checkUsername(user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(user));
    }

    @SignInUserDocs
    @PostMapping("/sign-in")
    public ResponseEntity<UserResponseDto> signIn(@RequestBody UserRequestDto user) {

        ValidationUtil.checkUsername(user.getUsername());

        return ResponseEntity.ok(authService.signIn(user));
    }
}
