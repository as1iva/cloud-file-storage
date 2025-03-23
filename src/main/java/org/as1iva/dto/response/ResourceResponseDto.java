package org.as1iva.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResourceResponseDto(String path, String name, Long size, String type) {

}
