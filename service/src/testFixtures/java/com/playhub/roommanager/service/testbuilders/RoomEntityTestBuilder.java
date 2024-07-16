package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aRoom")
@With
public class RoomEntityTestBuilder implements TestObjectBuilder<RoomEntity> {

    private UUID id = UUID.fromString("026fbd40-4e15-413d-a118-e63f85658c97");

    private UUID ownerId = UUID.fromString("428e8fac-af78-4d1f-af55-f9d6225d3111");

    private String securityCode = "code";

    private Integer maxParticipants = 10;

    private Set<RoomParticipantEntity> participants = new HashSet<>();

    private Instant createdAt = Instant.now();

    public static RoomEntityTestBuilder newRoom() {
        return aRoom()
                .withId(null)
                .withCreatedAt(null);
    }

    public static RoomEntityTestBuilder from(NewRoomRequest request) {
        return newRoom()
                .withOwnerId(request.ownerId())
                .withSecurityCode(request.securityCode())
                .withMaxParticipants(request.maxParticipants());
    }

    @Override
    public RoomEntity build() {
        RoomEntity room = new RoomEntity(id, ownerId, securityCode, maxParticipants, new HashSet<>(), createdAt);
        participants.forEach(participant -> participant.setRoom(room));
        return room;
    }

}
