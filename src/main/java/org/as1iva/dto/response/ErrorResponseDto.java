package org.as1iva.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponseDto(int status, String message) {

}
