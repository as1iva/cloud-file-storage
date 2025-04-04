package org.as1iva.docs.minio.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.as1iva.dto.response.ErrorResponseDto;
import org.as1iva.dto.response.ResourceResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        tags = {"Resource"},
        summary = "Find a resources",
        description = "Find a resources by the given query",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Resources were successfully found",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ResourceResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        [
                                                          {
                                                            "path": "",
                                                            "name": "folder",
                                                            "type": "DIRECTORY"
                                                          },
                                                          {
                                                            "path": "folder/",
                                                            "name": "new_folder",
                                                            "type": "DIRECTORY"
                                                          },
                                                          {
                                                            "path": "",
                                                            "name": "old_folder",
                                                            "type": "DIRECTORY"
                                                          }
                                                        ]
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Validation error",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 400,
                                                            "message": "Path is invalid"
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Authentication error",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 401,
                                                            "message": "Unauthorized"
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 500,
                                                            "message": "Internal server error"
                                                        }
                                                        """
                                        )
                                }
                        )
                )
        }
)
public @interface SearchResourceDocs {
}
