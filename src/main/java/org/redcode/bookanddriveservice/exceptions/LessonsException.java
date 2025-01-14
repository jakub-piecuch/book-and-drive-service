package org.redcode.bookanddriveservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LessonsException extends RuntimeException {

    private int statusCode;
    private ErrorDetails errorDetails;

    public LessonsException(
        String message,
        int statusCode,
        ErrorDetails errorDetails
    ) {
        super(message);
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }

    public static LessonsException of(HttpStatus status, ErrorDetails errorDetails) {
        return new LessonsException(
            errorDetails.getMessage(),
            status.value(),
            errorDetails
        );
    }
}
