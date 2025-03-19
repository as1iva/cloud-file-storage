package org.as1iva.service;

import org.as1iva.dto.request.UserRequestDto;
import org.as1iva.entity.User;
import org.as1iva.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Testcontainers
@SpringBootTest
public class AuthServiceIT {

    public static final String TEST_USERNAME = "test_login";

    public static final String TEST_PASSWORD = "test_password";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:17.2")
                    .withDatabaseName("testDatabase")
                    .withUsername("testUser")
                    .withPassword("testPass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void signUpUser_shouldReturnUserIfCredentialsUnique() {
        UserRequestDto userRegistrationRequestDto = UserRequestDto.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        authService.signUp(userRegistrationRequestDto);

        User user = userRepository.findByUsername(TEST_USERNAME)
                .orElseThrow(() -> new AssertionError("User not found in database"));

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void shouldThrowExceptionWhenUsernameNotUnique() {
        UserRequestDto userRegistrationRequestDto = UserRequestDto.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        authService.signUp(userRegistrationRequestDto);

        assertThatThrownBy(() -> authService.signUp(userRegistrationRequestDto))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
