package org.redcode.bookanddriveservice.exceptions;

import lombok.Getter;

@Getter
public class DuplicateTenantException extends RuntimeException {

    private final String reason;

    public DuplicateTenantException(
        String message,
        String reason
    ) {
        super(message);
        this.reason = reason;
    }

    public static DuplicateTenantException of(String message, String reason) {
        return new DuplicateTenantException(
            message,
            reason
        );
    }
}
