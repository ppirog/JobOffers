package joboffers.infrastructure.offer.controller.error;

import joboffers.domain.offer.NotFoundInDatabaseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
class OfferErrorHandler {

    @ExceptionHandler(NotFoundInDatabaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundInDatabaseResponseDto handleException(NotFoundInDatabaseException exception) {
        log.warn("NotFoundInDatabaseException - error while accesing offer by id");
        return NotFoundInDatabaseResponseDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
    }
}
