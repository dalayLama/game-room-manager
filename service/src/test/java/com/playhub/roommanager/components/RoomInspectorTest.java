package com.playhub.roommanager.components;

import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.exceptions.SecurityCodeInvalidException;
import com.playhub.roommanager.service.testbuilders.RoomEntityTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class RoomInspectorTest {

    @InjectMocks
    private RoomInspector roomInspector;

    @ParameterizedTest
    @CsvSource({"5555", ","})
    void shouldNotThrowAnyExceptionsIfRoomDoesNotHaveSecurityCode(String requestCode) {
        RoomEntity room = RoomEntityTestBuilder.aRoom().withSecurityCode(null).build();

        assertThatCode(() -> roomInspector.inspectSecurityCode(room, requestCode)).doesNotThrowAnyException();
    }

    @Test
    void shouldNotThrowAnyExceptionsIfSecurityCodeIsValid() {
        RoomEntity room = RoomEntityTestBuilder.aRoom().build();
        String requestCode = room.getSecurityCode();

        assertThatCode(() -> roomInspector.inspectSecurityCode(room, requestCode)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({"not valid", ","})
    void shouldThrowSecurityCodeInvalidExceptionIfSecurityCodeIsInvalid(String requestCode) {
        RoomEntity room = RoomEntityTestBuilder.aRoom().build();

        assertThatThrownBy(() -> roomInspector.inspectSecurityCode(room, requestCode))
                .isExactlyInstanceOf(SecurityCodeInvalidException.class);
    }

    @Test
    void shouldNotThrowAnyExceptionsIfRoomIsNotLimitedByMaxParticipants() {
        RoomEntity room = RoomEntityTestBuilder.aRoom().withMaxParticipants(null).build();
        int newParticipantsCount = Integer.MAX_VALUE;

        assertThatCode(() -> roomInspector.inspectCapacity(room, newParticipantsCount)).doesNotThrowAnyException();
    }

    @Test
    void shouldNotThrowAnyExceptionsIfNewParticipantsCountNotExceedRoomCapacity() {
        RoomEntity room = RoomEntityTestBuilder.aRoom().withMaxParticipants(1).build();

        assertThatCode(() -> roomInspector.inspectCapacity(room, 1)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowRoomCapacityExceededExceptionIfNewParticipantsCountExceedRoomCapacity() {
        RoomEntity room = RoomEntityTestBuilder.aRoom().withMaxParticipants(1).build();

        assertThatThrownBy(() -> roomInspector.inspectCapacity(room, 2));
    }

}