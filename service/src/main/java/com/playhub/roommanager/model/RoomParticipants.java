package com.playhub.roommanager.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoomParticipants(
        UUID id,
        UUID ownerId,
        String securityCode,
        Integer maxParticipants,
        Instant createdAt,
        List<RoomParticipant> participants
) {}
