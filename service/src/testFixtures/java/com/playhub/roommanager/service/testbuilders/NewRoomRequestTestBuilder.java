package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.jimbeam.test.utils.common.TestObjectBuilderUtils;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.restapi.dto.requests.NewRoomRequestDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@With
public class NewRoomRequestTestBuilder implements TestObjectBuilder<NewRoomRequest> {

    private UUID ownerId;

    private Integer maxParticipants;

    private String securityCode;

    private Set<UUID> participants;

    public static NewRoomRequestTestBuilder random() {
        return TestObjectBuilderUtils.randomObject(NewRoomRequestTestBuilder.class);
    }

    public static NewRoomRequestTestBuilder from(UUID ownerId, NewRoomRequestDto dto) {
        return new NewRoomRequestTestBuilder()
                .withOwnerId(ownerId)
                .withMaxParticipants(dto.maxParticipants())
                .withSecurityCode(dto.securityCode())
                .withParticipants(dto.participants());
    }

    @Override
    public NewRoomRequest build() {
        return new NewRoomRequest(ownerId, maxParticipants, securityCode, participants);
    }

}
