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
        summary = "Move or rename resource",
        description = "Move or rename resource",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Resource was successfully moved or renamed",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ResourceResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "path": "places/friends/",
                                                            "name": "saint-petersburg.pdf",
                                                            "size": 123,
                                                            "type": "FILE"
                                                        }
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
                        responseCode = "404",
                        description = "Resource does not exist",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 404,
                                                            "message": "Resource not found"
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Resource already exists at the target path",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 409,
                                                            "message": "Resource already exists"
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
public @interface MoveResourceDocs {
}
