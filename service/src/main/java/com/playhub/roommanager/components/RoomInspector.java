package com.playhub.roommanager.components;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.exceptions.RoomCapacityExceededException;
import com.playhub.roommanager.exceptions.SecurityCodeInvalidException;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class RoomInspector {

    public void inspectNewParticipant(RoomEntity room, NewParticipantRequest request) {
        inspectSecurityCode(room, request.securityCode());
        inspectCapacity(room, 1);
    }

    public void inspectSecurityCode(RoomEntity room, String securityCode) {
        if (Objects.isNull(room.getSecurityCode()) || Objects.equals(room.getSecurityCode(), securityCode)) {
            return;
        }
        throw new SecurityCodeInvalidException();
    }

    public void inspectCapacity(RoomEntity room, int newParticipantsCount) {
        if (Objects.isNull(room.getMaxParticipants())) {
            return;
        }
        int currentCapacity = room.getParticipants().size();
        if (currentCapacity + newParticipantsCount > room.getMaxParticipants()) {
            throw new RoomCapacityExceededException(currentCapacity, room.getMaxParticipants());
        }
    }

}
