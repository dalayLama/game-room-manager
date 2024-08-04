package com.playhub.roommanager.dao;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface RoomDao {

    Optional<RoomEntity> lockRoomForWriting(@NotNull UUID id);

    Optional<RoomEntity> findRoomById(@NotNull UUID id);

    RoomEntity saveRoom(@NotNull RoomEntity room);

    void deleteRoom(@NotNull RoomEntity room);

    void deleteRoom(@NotNull UUID roomId);

    Optional<RoomParticipantEntity> findByRoomIdAndParticipantId(@NotNull UUID roomId, @NotNull UUID participantId);

}
