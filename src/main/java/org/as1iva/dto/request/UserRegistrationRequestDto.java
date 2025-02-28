package org.as1iva.dto.request;


import lombok.Builder;

@Builder
public record UserRegistrationRequestDto(String username, String password, String confirmPassword) {

}
