package com.playhub.roommanager.services;

import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface RoomService {

    RoomParticipants createRoom(@NotNull @Valid NewRoomRequest request);

}
