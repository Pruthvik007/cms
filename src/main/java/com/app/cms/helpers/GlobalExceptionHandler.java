package com.app.cms.helpers;

import com.app.cms.exceptions.CmsException;
import com.app.cms.pojos.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle Validation Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((message1, message2) -> message1 + ", " + message2)
                .orElse("Invalid Input");
        logger.error("Validation Error: {}", errorMessage);
        Response<Void> response = Response.<Void>builder()
                .message(errorMessage)
                .status(Response.Status.FAILURE)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle all Business Exceptions
    @ExceptionHandler(CmsException.class)
    public ResponseEntity<Response<Void>> handleBusinessException(CmsException exception) {
        Response<Void> response = Response.<Void>builder()
                .message(exception.getMessage())
                .status(Response.Status.FAILURE)
                .build();
        logger.error("CmsException Occurred: {}", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle All Other Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneralException(Exception exception) {
        Response<Void> response = Response.<Void>builder()
                .status(Response.Status.ERROR)
                .build();
        logger.error("Exception Occurred: {}", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

