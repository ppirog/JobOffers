package joboffers.infrastructure.token.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
class TokenControllerErrorHandler {


    public static final String BAD_CREDENTIALS = "Bad credentials";

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public TokenErrorResponseDto handleBadCredentialsException() {
        log.error(BAD_CREDENTIALS);
        return TokenErrorResponseDto.builder().message(BAD_CREDENTIALS).status(HttpStatus.UNAUTHORIZED).build();
    }
}
