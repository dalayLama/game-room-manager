package com.playhub.roommanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class SecurityCodeInvalidException extends RoomManagerException {

    public SecurityCodeInvalidException() {
        super(ErrorType.SECURITY_CODE_INVALID);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public Object[] getDetailMessageArguments() {
        return new Object[0];
    }

}
