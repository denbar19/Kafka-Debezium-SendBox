package akvelon.zuora.denysenko.exception;

import akvelon.zuora.denysenko.exception.entity.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Denysenko Stanislav
 */
@Slf4j
@ControllerAdvice
@Component
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles situations while fetching, when input is not valid.
     *
     * @param exception What entity isn't found
     * @return What is wrong while fetching
     */
    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.info("handleEntityNotFoundException, {}", exception.getMessage());
        ApiError apiError = new ApiError(exception.getMessage());
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler({ChangesNotPersistedException.class})
    protected ResponseEntity<Object> handleChangesNotPersistedException(ChangesNotPersistedException exception) {
        log.info("handleChangesNotPersistedException, {}", exception.getMessage());
        ApiError apiError = new ApiError(exception.getMessage());
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(BadRequestException exception) {
        log.info("handleEntityNotFoundException, {}", exception.getMessage());
        ApiError apiError = new ApiError(exception.getMessage());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintValidationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraints = exception.getConstraintViolations();
        exception.getLocalizedMessage();
        if (Objects.isNull(constraints)) {
            return new ResponseEntity<>(new ApiError("No constraints"), BAD_REQUEST);
        }
        ConstraintViolation constraintViolation = constraints.iterator().next();
        log.info("handleEntityNotFoundException, {}, {}", constraintViolation.getMessage(), constraintViolation.getInvalidValue());
        ApiError apiError = new ApiError(constraintViolation.getMessage() + constraintViolation.getInvalidValue());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

}
