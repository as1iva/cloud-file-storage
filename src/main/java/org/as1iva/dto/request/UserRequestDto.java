package org.as1iva.dto.request;

import lombok.Builder;

@Builder
public record UserRequestDto(String username, String password) {

}
