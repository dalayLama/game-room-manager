package com.playhub.roommanager.components;

import com.playhub.autoconfigure.security.rest.secutiry.PlayHubUser;
import com.playhub.roommanager.dao.RoomDao;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.utils.RoomUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Component
@Validated
@RequiredArgsConstructor
public class RoomSecurityService {

    private final RoomDao roomDao;

    public boolean canDeleteRoom(@NotNull UUID roomId) {
        RoomEntity room = roomDao.findRoomById(roomId).orElse(null);
        if (room == null) {
            return true;
        }

        PlayHubUser currentUser = getCurrentUser();
        return RoomUtil.isRoomOwner(currentUser.getId(), room);
    }

    public boolean canDeleteParticipant(@NotNull UUID roomId, @NotNull UUID participantId) {
        RoomParticipantEntity participant = roomDao.findByRoomIdAndParticipantId(roomId, participantId)
                .orElse(null);
        if (participant == null) {
            return true;
        }

        UUID userId = getCurrentUser().getId();
        return userId.equals(participant.getParticipantId()) || RoomUtil.isRoomOwner(userId, participant.getRoom());
    }

    private PlayHubUser getCurrentUser() {
        return ((PlayHubUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
