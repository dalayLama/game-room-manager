package com.playhub.roommanager.exceptions;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
public enum ErrorMessageCode {

    NOT_FOUND(new String[]{"NotFound"}),

    ROOM_NOT_FOUND(NOT_FOUND, new String[]{"NotFound.room"}),

    ROOM_NOT_FOUND_BY_ID(ROOM_NOT_FOUND, new String[]{"NotFound.room.id"});

    private final String[] codes;

    ErrorMessageCode(String[] codes) {
        this.codes = codes;
    }

    ErrorMessageCode(ErrorMessageCode parent, String[] codes) {
        this.codes = Stream.concat(Arrays.stream(parent.getCodes()), Arrays.stream(codes)).toArray(String[]::new);
    }

    @Override
    public String toString() {
        return "ErrorMessageCode{" +
                "name=" + name() +
                ",codes=" + Arrays.toString(codes) +
                '}';
    }

}
