package joboffers.infrastructure.apivalidation;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
public class ApiValidationErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationErrorResponseDto handleException(MethodArgumentNotValidException exception) {
        log.warn("Validation error");
        return ApiValidationErrorResponseDto.builder()
                .errors(exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(MongoWriteException.class)
    public DuplicateKeyExceptionDto handleException(MongoWriteException exception) {
        log.warn("Duplicate key exception occurred." + exception.getClass());
        return DuplicateKeyExceptionDto.builder()
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
    }
}
