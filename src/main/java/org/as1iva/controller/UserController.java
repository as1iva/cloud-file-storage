package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.UserLoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserLoginResponseDto> getUser(@AuthenticationPrincipal UserDetails userDetails) {

        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
                .username(userDetails.getUsername())
                .build();

        return ResponseEntity.ok(userLoginResponseDto);
    }
}
