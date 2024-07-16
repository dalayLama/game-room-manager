package com.playhub.roommanager.services.impls;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.dao.repositories.RoomRepository;
import com.playhub.roommanager.mappers.RoomMapper;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.service.testbuilders.NewRoomRequestTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomEntityTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantsTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DaoRoomServiceTest {

    @Mock
    private RoomRepository repository;

    @Mock
    private RoomMapper mapper;

    @InjectMocks
    private DaoRoomService service;

    @Test
    void shouldCreateRoom() {
        NewRoomRequest request = NewRoomRequestTestBuilder.random().build();
        RoomEntity newEntity = RoomEntityTestBuilder.from(request).build();
        RoomParticipants expectedResult = RoomParticipantsTestBuilder.from(newEntity).build();

        SavingRoomMatcher matcher = new SavingRoomMatcher(request);
        when(mapper.toRoomEntity(request)).thenReturn(newEntity);
        when(repository.saveAndFlush(argThat(matcher))).thenReturn(newEntity);
        when(mapper.toRoomParticipants(newEntity)).thenReturn(expectedResult);

        RoomParticipants result = service.createRoom(request);
        assertThat(result).isSameAs(expectedResult);
    }

    @RequiredArgsConstructor
    private static class SavingRoomMatcher implements ArgumentMatcher<RoomEntity> {

        private final NewRoomRequest request;

        @Override
        public boolean matches(RoomEntity argument) {
            return Objects.isNull(argument.getId()) &&
                    Objects.isNull(argument.getCreatedAt()) &&
                    Objects.equals(request.ownerId(), argument.getOwnerId()) &&
                    Objects.equals(request.maxParticipants(), argument.getMaxParticipants()) &&
                    Objects.equals(request.securityCode(), argument.getSecurityCode()) &&
                    checkParticipants(argument);
        }

        private boolean checkParticipants(RoomEntity argument) {
            for (UUID participantId : request.participants()) {
                RoomParticipantEntity participant = argument.findParticipantById(participantId).orElseThrow();
                if (!checkParticipant(argument, participant, participantId)) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkParticipant(RoomEntity room, RoomParticipantEntity participant, UUID requiredParticipantId) {
            return Objects.equals(participant.getParticipantId(), requiredParticipantId) &&
                    Objects.equals(participant.getId().getRoom(), room) &&
                    Objects.isNull(participant.getAddedAt());
        }

    }

}