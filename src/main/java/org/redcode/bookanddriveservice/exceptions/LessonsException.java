package org.redcode.bookanddriveservice.exceptions;

import org.springframework.http.HttpStatus;

public class LessonsException extends RuntimeException {

    public LessonsException(String message) {
        super(message);
    }

    public static LessonsException of(HttpStatus status, ErrorDetails errorDetails) {
        return new LessonsException(errorDetails.getMessage());
    }
}
