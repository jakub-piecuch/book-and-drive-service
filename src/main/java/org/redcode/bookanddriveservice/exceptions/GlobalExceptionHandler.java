package org.redcode.bookanddriveservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle Generic Exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDetails> handleGenericException(
//        Exception ex, WebRequest request) {
//
//        log.error("Request: {}, has failed with exception.", request, ex);
//
//        ErrorDetails errorDetails = ErrorDetails.builder()
//            .timestamp(LocalDateTime.now())
//            .status(500)
//            .error(String.valueOf(ex.getClass()))
//            .message(ex.getMessage())
//            .build();
//
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
