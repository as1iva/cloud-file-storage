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
        summary = "Upload resource",
        description = "Upload resource",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Resource was successfully uploaded",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ResourceResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        [
                                                          {
                                                            "path": "friends/",
                                                            "name": "arturito.png",
                                                            "size": 123,
                                                            "type": "FILE"
                                                          },
                                                          {
                                                            "path": "friends/",
                                                            "name": "zhukov.jpg",
                                                            "size": 123,
                                                            "type": "FILE"
                                                          },
                                                          {
                                                            "path": "friends/",
                                                            "name": "dimas.pdf",
                                                            "size": 123
                                                            "type": "FILE"
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
public @interface UploadResourceDocs {
}
