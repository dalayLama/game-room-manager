package com.playhub.roommanager.dao;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.repositories.RoomParticipantRepository;
import com.playhub.roommanager.dao.repositories.RoomRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Validated
public class JpaRoomDao implements RoomDao {

    private final RoomRepository roomRepo;

    private final RoomParticipantRepository participantRepo;

    @Override
    public Optional<RoomEntity> lockRoomForWriting(@NotNull UUID id) {
        return roomRepo.pessimisticWrite(id);
    }

    @Override
    public RoomEntity saveRoom(@NotNull RoomEntity room) {
        return roomRepo.saveAndFlush(room);
    }

    @Override
    public void deleteRoom(@NotNull RoomEntity room) {
        deleteRoom(room.getId());
    }

    @Override
    public void deleteRoom(@NotNull UUID roomId) {
        participantRepo.deleteByRoomId(roomId);
        roomRepo.deleteById(roomId);
    }

}
