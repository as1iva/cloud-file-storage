package org.as1iva.docs.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.as1iva.dto.response.ErrorResponseDto;
import org.as1iva.dto.response.UserResponseDto;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        tags = {"Authorization"},
        summary = "Sign in user",
        description = "Sign in user and get user information",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Success",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UserResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "username": "MarkScott69"
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
                                                            "message": "Username must be at least 5 characters long"
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid credentials",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "status": 401,
                                                            "message": "Password or username are incorrect"
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
public @interface SignInUserDocs {
}
