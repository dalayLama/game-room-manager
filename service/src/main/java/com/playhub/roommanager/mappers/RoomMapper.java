package com.playhub.roommanager.mappers;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.model.Room;
import com.playhub.roommanager.model.RoomParticipant;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.restapi.dto.requests.NewRoomRequestDto;
import com.playhub.roommanager.restapi.dto.responses.RoomParticipantsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    RoomEntity toRoomEntity(NewRoomRequest request);

    Room toRoom(RoomEntity entity);

    RoomParticipants toRoomParticipants(RoomEntity entity);

    @Mapping(target = "id", source = "id.participantId")
    RoomParticipant toRoomParticipant(RoomParticipantEntity entity);

    List<RoomParticipant> toRoomParticipants(Collection<? extends RoomParticipantEntity> entities);

    NewRoomRequest toRequest(UUID ownerId, NewRoomRequestDto dto);

    RoomParticipantsDto toDto(RoomParticipants roomParticipants);

    default UUID toParticipantId(RoomParticipant roomParticipant) {
        return roomParticipant.id();
    }

}
