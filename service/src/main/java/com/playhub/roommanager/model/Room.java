package com.playhub.roommanager.model;

import java.time.Instant;
import java.util.UUID;

public record Room(
        UUID id,
        UUID ownerId,
        String securityCode,
        Integer maxParticipants,
        Instant createdAt
) {}
