package com.playhub.roommanager.mappers;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.model.Room;
import com.playhub.roommanager.model.RoomParticipant;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.restapi.dto.requests.NewRoomRequestDto;
import com.playhub.roommanager.restapi.dto.responses.RoomParticipantsDto;
import com.playhub.roommanager.service.testbuilders.NewRoomRequestDtoTestBuilder;
import com.playhub.roommanager.service.testbuilders.NewRoomRequestTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomEntityTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantEntityTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantsDtoTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantsTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomTestBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RoomMapperTest {

    private final RoomMapper mapper = Mappers.getMapper(RoomMapper.class);

    @Test
    void shouldConvertNewRoomRequestToRoomEntity() {
        NewRoomRequest request = NewRoomRequestTestBuilder.random().build();
        RoomEntity expectedResult = RoomEntityTestBuilder.from(request).build();

        RoomEntity result = mapper.toRoomEntity(request);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void shouldConvertRoomEntityToRoom() {
        RoomEntity entity = RoomEntityTestBuilder.aRoom().build();
        Room expectedResult = RoomTestBuilder.from(entity).build();

        Room result = mapper.toRoom(entity);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void shouldConvertRoomEntityToRoomParticipants() {
        RoomEntity room = RoomEntityTestBuilder.aRoom()
                .withParticipants(Set.of(
                        RoomParticipantEntityTestBuilder.aParticipant().build(),
                        RoomParticipantEntityTestBuilder.aParticipant().build()
                ))
                .build();
        RoomParticipants expectedResult = RoomParticipantsTestBuilder.from(room).build();

        RoomParticipants result = mapper.toRoomParticipants(room);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void shouldConvertRoomParticipantEntityToRoomParticipant() {
        RoomParticipantEntity entity = RoomParticipantEntityTestBuilder.aParticipant().build();
        RoomParticipant expectedResult = RoomParticipantTestBuilder.from(entity).build();

        RoomParticipant result = mapper.toRoomParticipant(entity);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void shouldConvertToNewRoomRequest() {
        UUID ownerId = UUID.fromString("f94f32f4-308f-4f71-84ec-521f5ba7b57c");
        NewRoomRequestDto requestDto = NewRoomRequestDtoTestBuilder.random().build();
        NewRoomRequest expectedResult = NewRoomRequestTestBuilder.from(ownerId, requestDto).build();

        NewRoomRequest result = mapper.toRequest(ownerId, requestDto);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void shouldConvertRoomParticipantsToRoomParticipantsDto() {
        RoomParticipants roomParticipants = RoomParticipantsTestBuilder.aRoom()
                .withParticipants(List.of(
                        RoomParticipantTestBuilder.aParticipant().build(),
                        RoomParticipantTestBuilder.aParticipant().build()
                ))
                .build();
        RoomParticipantsDto expectedResult = RoomParticipantsDtoTestBuilder.from(roomParticipants).build();

        RoomParticipantsDto result = mapper.toDto(roomParticipants);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void shouldConvertRoomParticipantToParticipantId() {
        RoomParticipant roomParticipant = RoomParticipantTestBuilder.aParticipant().build();
        UUID expectedResult = roomParticipant.id();

        UUID result = mapper.toParticipantId(roomParticipant);

        assertThat(result).isEqualTo(expectedResult);
    }

}