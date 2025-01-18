package org.redcode.bookanddriveservice.exceptions;

public class DuplicateSchemaException extends RuntimeException {

    private final String reason;

    public DuplicateSchemaException(
        String message,
        String reason
    ) {
        super(message);
        this.reason = reason;
    }

    public static DuplicateSchemaException of(String message, String reason) {
        return new DuplicateSchemaException(
            message,
            reason
        );
    }
}
