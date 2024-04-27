package joboffers.domain.loginandregister;

import joboffers.domain.loginandregister.dto.UserMessageDto;
import joboffers.domain.loginandregister.dto.UserRequestDto;
import joboffers.domain.loginandregister.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginAndRegisterFacadeTest {


    @Test
    void registerUserSucceded() {
        // Given
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
                new InMemoryUserRepositoryTestImpl(),
                new UserMapper(
                        new IdGeneratorTestImpl()
                )
        );

        // When
        final UserMessageDto register = loginAndRegisterFacade.register(
                UserRequestDto.builder()
                        .username("username")
                        .password("password")
                        .build()
        );


        // Then
        assertAll(
                () -> assertEquals("User registered", register.message()),
                () -> assertFalse(register.userRequestDto().isAdmin())

        );
    }


    @Test
    void registerUserImpossible() {
        // Given
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
                new InMemoryUserRepositoryTestImpl(),
                new UserMapper(
                        new IdGeneratorTestImpl()
                )
        );

        loginAndRegisterFacade.register(
                UserRequestDto.builder()
                        .username("username")
                        .password("password")
                        .build()
        );
        // When
        final UserMessageDto register = loginAndRegisterFacade.register(
                UserRequestDto.builder()
                        .username("username")
                        .password("password")
                        .build()
        );
        // Then
        assertEquals("User already exists", register.message());
    }

    @Test
    void findByUsername() {
        // Given
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
                new InMemoryUserRepositoryTestImpl(),
                new UserMapper(
                        new IdGenerator()
                )
        );

        loginAndRegisterFacade.register(
                UserRequestDto.builder()
                        .username("username1")
                        .password("password")
                        .build()
        );
        // When
        final UserResponseDto username = loginAndRegisterFacade.findByUsername("username1");
        // Then
        assertEquals("username1", username.username());
    }

    @Test
    void findByUsernameNotFound() {
        // Given
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
                new InMemoryUserRepositoryTestImpl(),
                new UserMapper(
                        new IdGenerator()
                )
        );

        assertThrows(BadCredentialsException.class, () -> loginAndRegisterFacade.findByUsername("username1"));

    }

}