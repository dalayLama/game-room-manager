package com.playhub.roommanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RoomCapacityExceededException extends RoomManagerException {

    private static final String MESSAGE_TEMPLATE = "Room capacity exceeded: current capacity - %d, max capacity - %d";

    private final int currentCapacity;

    private final Integer maxCapacity;

    public RoomCapacityExceededException(int currentCapacity, Integer maxCapacity) {
        super(MESSAGE_TEMPLATE.formatted(currentCapacity, maxCapacity), ErrorType.ROOM_CAPACITY_EXCEEDED);
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
    }

    @Override
    public Object[] getDetailMessageArguments() {
        return new Object[] {currentCapacity, maxCapacity};
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }

}
