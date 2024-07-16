package com.playhub.roommanager.model;

import java.time.Instant;
import java.util.UUID;

public record RoomParticipant(
        UUID id,
        Instant addedAt
) {}
