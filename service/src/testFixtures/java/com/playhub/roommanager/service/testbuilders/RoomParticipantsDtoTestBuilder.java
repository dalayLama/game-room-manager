package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.playhub.roommanager.model.RoomParticipant;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.restapi.dto.responses.RoomParticipantsDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(staticName = "aDto")
@AllArgsConstructor
@With
public class RoomParticipantsDtoTestBuilder implements TestObjectBuilder<RoomParticipantsDto> {

    private UUID id = UUID.fromString("026fbd40-4e15-413d-a118-e63f85658c97");

    private UUID ownerId = UUID.fromString("428e8fac-af78-4d1f-af55-f9d6225d3111");

    private Integer maxParticipants = 10;

    private Set<UUID> participants = new HashSet<>();

    private Instant createdAt = Instant.now();

    public static RoomParticipantsDtoTestBuilder from(RoomParticipants roomParticipants) {
        Set<UUID> participants = roomParticipants.participants().stream()
                .map(RoomParticipant::id)
                .collect(Collectors.toSet());
        return aDto()
                .withId(roomParticipants.id())
                .withOwnerId(roomParticipants.ownerId())
                .withMaxParticipants(roomParticipants.maxParticipants())
                .withParticipants(participants)
                .withCreatedAt(roomParticipants.createdAt());
    }

    @Override
    public RoomParticipantsDto build() {
        return new RoomParticipantsDto(id, ownerId, maxParticipants, createdAt, participants);
    }

}
