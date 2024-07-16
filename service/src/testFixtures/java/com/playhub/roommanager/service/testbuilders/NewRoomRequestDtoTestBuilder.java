package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.jimbeam.test.utils.common.TestObjectBuilderUtils;
import com.playhub.roommanager.restapi.dto.requests.NewRoomRequestDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@With
public class NewRoomRequestDtoTestBuilder implements TestObjectBuilder<NewRoomRequestDto> {

    private Integer maxParticipants;

    private String securityCode;

    private Set<UUID> participants;

    public static NewRoomRequestDtoTestBuilder random() {
        return TestObjectBuilderUtils.randomObject(NewRoomRequestDtoTestBuilder.class);
    }

    @Override
    public NewRoomRequestDto build() {
        return new NewRoomRequestDto(maxParticipants, securityCode, participants);
    }

}
