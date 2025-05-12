package ru.peredera.mock.rest.error;

import jakarta.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.function.ServerResponse;
import ru.peredera.mock.rest.error.exceptions.MockRestException;
import ru.peredera.mock.rest.error.model.ErrorDto;
import ru.peredera.mock.rest.error.model.ErrorRs;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Глобальный обработчики ошибок для сервиса
 */
@Slf4j
@ControllerAdvice
public class ErrorHandler {

    //TODO Нефункциональные требования - обработка ошибок
    private final ErrorService errorService;

    @Autowired
    public ErrorHandler(ErrorService errorService) {
        this.errorService = errorService;
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception ex) {
        return buildErrorResponse(errorService.getError(ex));
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidateException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildErrorResponse(errorService.getError(MockRestException.builder().errorCode("validate_error").message(ex.getMessage()).build()));
    }

    public ServerResponse buildServerResponse(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Optional.of(errorService.getError(ex))
                .map(errorDesc -> ServerResponse
                        .status(requireNonNull(HttpStatus.resolve(errorDesc.httpStatus())))
                        .body(new ErrorRs(errorDesc.errorCode(), errorDesc.errorMessage()))
                )
                .orElse(ServerResponse.notFound().build());
    }

    private ResponseEntity<ErrorRs> buildErrorResponse(ErrorDto errorDesc) {
        return new ResponseEntity<>(
                new ErrorRs(errorDesc.errorCode(), errorDesc.errorMessage()),
                HttpStatusCode.valueOf(errorDesc.httpStatus())
        );
    }
}
