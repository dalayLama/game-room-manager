package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.model.RoomParticipant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor(staticName = "aParticipant")
@AllArgsConstructor
@With
public class RoomParticipantTestBuilder implements TestObjectBuilder<RoomParticipant> {

    private UUID id = UUID.randomUUID();

    private Instant addedAt = Instant.now();

    public static RoomParticipantTestBuilder from(RoomParticipantEntity entity) {
        return aParticipant()
                .withId(entity.getParticipantId())
                .withAddedAt(entity.getAddedAt());
    }

    @Override
    public RoomParticipant build() {
        return new RoomParticipant(id, addedAt);
    }

}
