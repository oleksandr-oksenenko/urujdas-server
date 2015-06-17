package urujdas.web.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import urujdas.dao.exception.NotFoundException;
import urujdas.web.common.model.error.ErrorResponse;
import urujdas.web.common.model.error.ErrorType;
import urujdas.web.common.model.error.NotFoundErrorResponse;
import urujdas.web.common.model.error.ValidationErrorResponse;
import urujdas.web.exception.ValidationException;

@ControllerAdvice(annotations = RestController.class)
public class CommonExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> validationException(ValidationException e) {
        LOG.debug("Validation error", e);

        return new ResponseEntity<>(
                new ValidationErrorResponse(e.getErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundErrorResponse> notFoundException(NotFoundException e) {
        LOG.debug("Entity of class = {} with id = {} was not found");

        return new ResponseEntity<>(
                new NotFoundErrorResponse(e.getEntityClass().getSimpleName(), e.getId()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> messageNotReadable(HttpMessageNotReadableException e) {
        LOG.debug("Message is not readable", e);

        return new ResponseEntity<>(
                new ErrorResponse(ErrorType.WRONG_FORMAT, "Payload is incorrect"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericExceptionHandler(Exception e) {
        LOG.error("Exception happened", e);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
