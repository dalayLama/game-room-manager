package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.model.RoomParticipant;
import com.playhub.roommanager.model.RoomParticipants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(staticName = "aRoom")
@AllArgsConstructor
@With
public class RoomParticipantsTestBuilder implements TestObjectBuilder<RoomParticipants> {

    private UUID id = UUID.fromString("026fbd40-4e15-413d-a118-e63f85658c97");

    private UUID ownerId = UUID.fromString("428e8fac-af78-4d1f-af55-f9d6225d3111");

    private String securityCode = "code";

    private Integer maxParticipants = 10;

    private List<RoomParticipant> participants = new ArrayList<>();

    private Instant createdAt = Instant.now();

    public static RoomParticipantsTestBuilder from(RoomEntity entity) {
        List<RoomParticipant> participants = entity.getParticipants().stream()
                .map(RoomParticipantTestBuilder::from)
                .map(RoomParticipantTestBuilder::build)
                .toList();
        return aRoom()
                .withId(entity.getId())
                .withOwnerId(entity.getOwnerId())
                .withSecurityCode(entity.getSecurityCode())
                .withMaxParticipants(entity.getMaxParticipants())
                .withParticipants(participants)
                .withCreatedAt(entity.getCreatedAt());

    }

    @Override
    public RoomParticipants build() {
        return new RoomParticipants(id, ownerId, securityCode, maxParticipants, createdAt, participants);
    }

}
