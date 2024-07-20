package com.playhub.roommanager.restapi.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewParticipantRequestDto(
        @NotNull UUID participantId,
        @Size(min = 4, max = 4) String securityCode)
{}
