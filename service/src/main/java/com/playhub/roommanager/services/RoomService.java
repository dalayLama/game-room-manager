package com.playhub.roommanager.services;

import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface RoomService {

    RoomParticipants createRoom(@NotNull @Valid NewRoomRequest request);

    RoomParticipants addParticipant(@NotNull UUID roomId, @NotNull @Valid NewParticipantRequest request);

    void deleteParticipant(@NotNull UUID roomId, @NotNull UUID participantId);

    void deleteRoom(@NotNull UUID id);

}
