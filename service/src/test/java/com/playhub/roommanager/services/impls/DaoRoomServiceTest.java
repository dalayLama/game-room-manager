package com.playhub.roommanager.services.impls;

import com.playhub.roommanager.components.RoomInspector;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.dao.repositories.RoomRepository;
import com.playhub.roommanager.mappers.RoomMapper;
import com.playhub.roommanager.model.RoomParticipant;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.service.testbuilders.NewParticipantRequestTestBuilder;
import com.playhub.roommanager.service.testbuilders.NewRoomRequestTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomEntityTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantEntityTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomParticipantsTestBuilder;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DaoRoomServiceTest {

    @Mock
    private RoomRepository repository;

    @Mock
    private RoomMapper mapper;

    @Mock
    private RoomInspector inspector;

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

    @Test
    void shouldAddParticipant() {
        UUID roomId = UUID.fromString("7aedb61f-8991-4806-b3ea-c815a4b5d253");
        RoomEntity room = RoomEntityTestBuilder.aRoom().build();
        NewParticipantRequest request = NewParticipantRequestTestBuilder.aRequest().build();

        RoomParticipants mockedResult = RoomParticipantsTestBuilder.aRoom().build();

        SavingParticipantMatcher matcher = new SavingParticipantMatcher(request);
        when(repository.pessimisticWrite(roomId)).thenReturn(Optional.of(room));
        when(repository.saveAndFlush(argThat(matcher))).thenAnswer(returnsFirstArg());
        when(mapper.toRoomParticipants(room)).thenReturn(mockedResult);

        RoomParticipants result = service.addParticipant(roomId, request);

        verify(inspector).inspectNewParticipant(room, request);
        assertThat(result).isSameAs(mockedResult);
    }

    @Test
    void shouldNotAddParticipantIfParticipantExists() {
        UUID roomId = UUID.fromString("7aedb61f-8991-4806-b3ea-c815a4b5d253");
        NewParticipantRequest request = NewParticipantRequestTestBuilder.aRequest().build();
        RoomEntity room = RoomEntityTestBuilder.aRoom()
                .withParticipants(Set.of(
                        RoomParticipantEntityTestBuilder.aParticipant().withParticipantId(request.participantId()).build()
                ))
                .build();

        RoomParticipants mockedResult = RoomParticipantsTestBuilder.aRoom().build();

        when(repository.pessimisticWrite(roomId)).thenReturn(Optional.of(room));
        when(mapper.toRoomParticipants(room)).thenReturn(mockedResult);

        RoomParticipants result = service.addParticipant(roomId, request);

        verify(inspector, never()).inspectNewParticipant(room, request);
        verify(repository, never()).saveAndFlush(any());
        assertThat(result).isSameAs(mockedResult);
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
                    Objects.equals(participant.getRoom(), room) &&
                    Objects.isNull(participant.getAddedAt());
        }

    }

    @RequiredArgsConstructor
    private static class SavingParticipantMatcher implements ArgumentMatcher<RoomEntity> {

        private final NewParticipantRequest request;

        @Override
        public boolean matches(RoomEntity argument) {
            return argument.getParticipants().stream()
                    .anyMatch(p -> Objects.equals(p.getParticipantId(), request.participantId()));
        }

    }

}