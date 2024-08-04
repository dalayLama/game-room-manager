package com.playhub.roommanager.services.impls;

import com.playhub.roommanager.components.RoomInspector;
import com.playhub.roommanager.dao.RoomDao;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.events.DomainEventType;
import com.playhub.roommanager.events.ParticipantEvent;
import com.playhub.roommanager.events.RoomEvent;
import com.playhub.roommanager.exceptions.RoomNotFoundByIdException;
import com.playhub.roommanager.mappers.RoomMapper;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.services.RoomService;
import com.playhub.roommanager.utils.RoomUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

    private final RoomDao dao;

    private final RoomMapper mapper;

    private final RoomInspector roomInspector;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public RoomParticipants createRoom(@NotNull @Valid NewRoomRequest request) {
        RoomEntity room = mapper.toRoomEntity(request);
        request.participants().stream()
                .map(RoomParticipantEntity::newParticipant)
                .forEach(room::addParticipant);
        RoomEntity savedRoom = dao.saveRoom(room);
        RoomParticipants roomParticipants = mapper.toRoomParticipants(savedRoom);
        eventPublisher.publishEvent(new RoomEvent(this, DomainEventType.NEW, roomParticipants));
        return roomParticipants;
    }

    @Override
    @Transactional
    public RoomParticipants addParticipant(@NotNull UUID roomId, @NotNull @Valid NewParticipantRequest request) {
        RoomEntity room = getById(dao::lockRoomForWriting, roomId);
        room.findParticipantById(request.participantId()).ifPresentOrElse(
                p -> log.info(
                        "Participant with id {} already exists in the room with id {}", request.participantId(), roomId
                ),
                () -> addParticipantToRoom(room, request)
        );
        return mapper.toRoomParticipants(room);
    }

    @Override
    @Transactional
    public void deleteParticipant(@NotNull UUID roomId, @NotNull UUID participantId) {
        RoomEntity room = getById(dao::lockRoomForWriting, roomId);
        room.findParticipantById(participantId).ifPresentOrElse(
                p -> deleteParticipant(room, p),
                () -> log.warn(
                        "Participant with id {} does not exist in the room with id {}", participantId, roomId
                )
        );
    }

    @Override
    @Transactional
    public void deleteRoom(@NotNull UUID id) {
        RoomEntity room = getById(dao::lockRoomForWriting, id);
        dao.deleteRoom(room);
    }

    private void deleteParticipant(RoomEntity room, RoomParticipantEntity participant) {
        if (RoomUtil.isRoomOwner(participant, room) || room.getParticipants().size() < 2) {
            dao.deleteRoom(room);
        } else {
            room.deleteParticipant(participant);
            dao.saveRoom(room);
            ParticipantEvent event = new ParticipantEvent(
                    this,
                    DomainEventType.DELETE,
                    mapper.toRoom(room),
                    mapper.toRoomParticipant(participant)
            );
            eventPublisher.publishEvent(event);
        }
    }

    private void addParticipantToRoom(RoomEntity room, NewParticipantRequest request) {
        roomInspector.inspectNewParticipant(room, request);
        RoomParticipantEntity newParticipant = RoomParticipantEntity.newParticipant(request.participantId());
        room.addParticipant(newParticipant);
        dao.saveRoom(room);
        room.findParticipantById(request.participantId()).ifPresent(p -> {
            ParticipantEvent event = new ParticipantEvent(
                    this,
                    DomainEventType.NEW,
                    mapper.toRoom(room),
                    mapper.toRoomParticipant(p)
            );
            eventPublisher.publishEvent(event);
        });
    }

    private RoomEntity getById(Function<UUID, Optional<RoomEntity>> function, UUID id) {
        return function.apply(id).orElseThrow(() -> new RoomNotFoundByIdException(id));
    }

}
