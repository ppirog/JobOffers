package joboffers.infrastructure.loginandregister.controller;

import jakarta.validation.Valid;
import joboffers.domain.loginandregister.LoginAndRegisterFacade;
import joboffers.domain.loginandregister.dto.UserMessageDto;
import joboffers.domain.loginandregister.dto.UserRequestDto;
import joboffers.infrastructure.loginandregister.controller.dto.LoginRequestDto;
import joboffers.infrastructure.loginandregister.controller.dto.LoginResponseDto;
import joboffers.infrastructure.loginandregister.controller.dto.RegisterRequestDto;
import joboffers.infrastructure.loginandregister.controller.dto.RegisterResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
@AllArgsConstructor
public class LoginAndRegisterRestController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {

        final String encode = passwordEncoder.encode(registerRequestDto.password());
        log.info("Registering user: " + registerRequestDto.username());
        final UserMessageDto register = loginAndRegisterFacade.register(UserRequestDto.builder()
                .username(registerRequestDto.username())
                .password(encode)
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                RegisterResponseDto.builder()
                        .status(HttpStatus.CREATED)
                        .message(register.message())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        log.info("Logging in user: " + loginRequestDto.username());

        loginAndRegisterFacade.findByUsername(loginRequestDto.username());

        return ResponseEntity.ok(LoginResponseDto.builder()
                .status(HttpStatus.OK)
                .message("User logged in")
                .build());
    }


}
