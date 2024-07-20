package com.playhub.roommanager.exceptions;

import java.util.UUID;

public class RoomNotFoundByIdException extends NotFoundException {

    private static final String MESSAGE_TEMPLATE = "Room with id %s not found";

    private final UUID id;

    public RoomNotFoundByIdException(UUID id) {
        super(MESSAGE_TEMPLATE.formatted(id), ErrorType.ROOM_NOT_FOUND_BY_ID);
        this.id = id;
    }

    @Override
    public Object[] getDetailMessageArguments() {
        return new Object[] {id};
    }

}
