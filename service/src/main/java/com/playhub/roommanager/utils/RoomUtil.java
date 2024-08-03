package com.playhub.roommanager.utils;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class RoomUtil {

    public static boolean isRoomOwner(UUID participantId, RoomEntity room) {
        return Objects.equals(room.getOwnerId(), participantId);
    }

    public static boolean isRoomOwner(RoomParticipantEntity participant, RoomEntity room) {
        return isRoomOwner(participant.getParticipantId(), room);
    }

}
