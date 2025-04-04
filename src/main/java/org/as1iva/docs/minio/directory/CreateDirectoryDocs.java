package org.as1iva.docs.minio.directory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.as1iva.dto.response.ErrorResponseDto;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.dto.response.UserResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        tags = {"Directory"},
        summary = "Create directory",
        description = "Create empty directory for user",
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Empty directory created",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ResourceResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "path": "",
                                                            "name": "miami_2011",
                                                            "type": "DIRECTORY"
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
                        description = "Parent folder in path does not exist",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 404,
                                                            "message": "Parent directory does not exist"
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Folder with that name already exists",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 409,
                                                            "message": "Directory already exists"
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
public @interface CreateDirectoryDocs {
}
