package com.playhub.roommanager.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewParticipantRequest(
        @NotNull UUID participantId,
        @Size(min = 4, max = 4) String securityCode
) {}
