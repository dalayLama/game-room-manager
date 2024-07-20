package com.playhub.roommanager.exceptions;

import com.playhub.common.exceptions.PlayhubException;

public abstract class RoomManagerException extends PlayhubException {

    private final ErrorType errorType;

    protected RoomManagerException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public RoomManagerException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public RoomManagerException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    public RoomManagerException(Throwable cause, ErrorType errorType) {
        super(cause);
        this.errorType = errorType;
    }

    public RoomManagerException(String message,
                                Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace,
                                ErrorType errorType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorType = errorType;
    }

    @Override
    public String getErrorCode() {
        return errorType.getErrorCode();
    }

    @Override
    public String getMessageSourceResolvableTitle() {
        return errorType.getTitleCode();
    }

    @Override
    public String getMessageSourceResolvableDetailMessage() {
        return errorType.getMessageCode();
    }

}
