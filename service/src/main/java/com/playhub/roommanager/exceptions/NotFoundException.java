package com.playhub.roommanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public abstract class NotFoundException extends RoomManagerException {

    protected NotFoundException(ErrorType errorType) {
        super(errorType);
    }

    public NotFoundException(String message, ErrorType errorType) {
        super(message, errorType);
    }

    public NotFoundException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause, errorType);
    }

    public NotFoundException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }

    public NotFoundException(String message,
                             Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace,
                             ErrorType errorType) {
        super(message, cause, enableSuppression, writableStackTrace, errorType);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

}
