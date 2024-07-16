package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aParticipant")
@With
public class RoomParticipantEntityTestBuilder implements TestObjectBuilder<RoomParticipantEntity> {

    private RoomParticipantId id  = new RoomParticipantId(RoomEntityTestBuilder.aRoom().build(), UUID.randomUUID());

    private Instant addedAt = Instant.now();

    public static RoomParticipantEntityTestBuilder newParticipant() {
        return aParticipant()
                .withId(new RoomParticipantId(null, UUID.randomUUID()))
                .withAddedAt(null);
    }

    public static RoomParticipantEntityTestBuilder aParticipant(RoomEntity room) {
        return aParticipant()
                .withId(new RoomParticipantId(room, UUID.randomUUID()));
    }

    @Override
    public RoomParticipantEntity build() {
        return new RoomParticipantEntity(id, addedAt);
    }
}
