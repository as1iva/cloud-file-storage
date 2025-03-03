package org.as1iva.dto.request;

import lombok.Builder;

@Builder
public record UserLoginRequestDto(String username, String password) {

}
