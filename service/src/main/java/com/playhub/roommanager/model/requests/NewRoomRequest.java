package com.playhub.roommanager.model.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record NewRoomRequest(
        @NotNull UUID ownerId,
        @Min(1) Integer maxParticipants,
        @Size(min = 4, max = 4) String securityCode,
        Set<UUID> participants
) {}