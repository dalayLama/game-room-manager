package com.playhub.roommanager.services.impls;

import com.playhub.roommanager.components.RoomInspector;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.dao.repositories.RoomRepository;
import com.playhub.roommanager.exceptions.RoomNotFoundByIdException;
import com.playhub.roommanager.mappers.RoomMapper;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.services.RoomService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class DaoRoomService implements RoomService {

    private final RoomRepository repository;

    private final RoomMapper mapper;

    private final RoomInspector roomInspector;

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

    @Override
    @Transactional
    public RoomParticipants addParticipant(@NotNull UUID roomId, @NotNull @Valid NewParticipantRequest request) {
        RoomEntity room = getById(repository::pessimisticWrite, roomId);
        room.findParticipantById(request.participantId()).ifPresentOrElse(
                p -> log.info(
                        "Participant with id {} already exists in the room with id {}", request.participantId(), roomId
                ),
                () -> {
                    addParticipantToRoom(room, request);
                    repository.saveAndFlush(room);
                }
        );
        return mapper.toRoomParticipants(room);
    }

    private void addParticipantToRoom(RoomEntity room, NewParticipantRequest request) {
        roomInspector.inspectNewParticipant(room, request);
        RoomParticipantEntity newParticipant = RoomParticipantEntity.newParticipant(request.participantId());
        room.addParticipant(newParticipant);
    }

    private RoomEntity getById(Function<UUID, Optional<RoomEntity>> function, UUID id) {
        return function.apply(id).orElseThrow(() -> new RoomNotFoundByIdException(id));
    }

}
