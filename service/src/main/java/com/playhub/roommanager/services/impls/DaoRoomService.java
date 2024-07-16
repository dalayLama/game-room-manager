package com.playhub.roommanager.services.impls;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.dao.repositories.RoomRepository;
import com.playhub.roommanager.mappers.RoomMapper;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.services.RoomService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class DaoRoomService implements RoomService {

    private final RoomRepository repository;

    private final RoomMapper mapper;

    @Override
    @Transactional
    public RoomParticipants createRoom(@NotNull @Valid NewRoomRequest request) {
        RoomEntity room = mapper.toRoomEntity(request);
        request.participants().stream()
                .map(RoomParticipantEntity::newParticipant)
                .forEach(room::addParticipant);
        RoomEntity savedRoom = repository.saveAndFlush(room);
        return mapper.toRoomParticipants(savedRoom);
    }

}
