package com.playhub.roommanager.restapi.dto.responses;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record RoomParticipantsDto(
        UUID id,
        UUID ownerId,
        Integer maxParticipants,
        Instant createdAt,
        Set<UUID> participants
) {}
