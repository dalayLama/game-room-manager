package com.playhub.roommanager.service.testbuilders;

import com.jimbeam.test.utils.common.TestObjectBuilder;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aRequest")
@With
public class NewParticipantRequestTestBuilder implements TestObjectBuilder<NewParticipantRequest> {

    private UUID participantId = UUID.fromString("54b9ba23-6f7b-4f29-845d-02b2d2e7b6d7");

    private String securityCode = null;

    public static NewParticipantRequestTestBuilder secured() {
        return aRequest()
                .withSecurityCode("5555");
    }

    @Override
    public NewParticipantRequest build() {
        return new NewParticipantRequest(participantId, securityCode);
    }

}
