package com.playhub.roommanager.dao;

import com.playhub.roommanager.dao.entities.RoomEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface RoomDao {

    Optional<RoomEntity> lockRoomForWriting(@NotNull UUID id);

    RoomEntity saveRoom(@NotNull RoomEntity room);

    void deleteRoom(@NotNull RoomEntity room);

    void deleteRoom(@NotNull UUID roomId);

}
