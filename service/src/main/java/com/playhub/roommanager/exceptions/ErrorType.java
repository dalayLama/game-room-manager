package com.playhub.roommanager.exceptions;

import lombok.Getter;

@Getter
public enum ErrorType {

    ROOM_NOT_FOUND,

    ROOM_NOT_FOUND_BY_ID(ROOM_NOT_FOUND, "id"),

    SECURITY_CODE_INVALID,

    ROOM_CAPACITY_EXCEEDED;

    private final String errorCode;

    private final String titleCode;

    private final String messageCode;

    ErrorType(ErrorType parent, String messageCode) {
        this.errorCode = parent.errorCode;
        this.titleCode = parent.titleCode;
        this.messageCode = messageCode(parent, messageCode);
    }

    ErrorType() {
        this.errorCode = name();
        this.titleCode = titleCode(name().toLowerCase());
        this.messageCode = messageCode(name().toLowerCase());
    }

    ErrorType(String errorCode, String titleCode, String messageCode) {
        this.errorCode = errorCode;
        this.titleCode = titleCode(titleCode);
        this.messageCode = messageCode(messageCode);
    }

    private String titleCode(String code) {
        return "%s.%s".formatted(code, "title");
    }

    private String messageCode(String code) {
        return "%s.%s".formatted(code, "message");
    }

    private String messageCode(ErrorType parent, String messageCode) {
        return "%s.%s".formatted(parent.errorCode.toLowerCase(), messageCode(messageCode));
    }

}
