package com.playhub.roommanager.dao.repositories;

import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipantEntity, Long> {

    @Query("DELETE FROM RoomParticipantEntity p where p.room.id = :roomId")
    @Modifying(flushAutomatically = true)
    void deleteByRoomId(@NotNull @Param("roomId") UUID roomId);

    @EntityGraph(attributePaths = {"room"})
    @Query("SELECT p FROM RoomParticipantEntity p where p.room.id = :roomId and p.participantId = :participantId")
    Optional<RoomParticipantEntity> findByRoomIdAndParticipantId(@NotNull @Param("roomId") UUID roomId,
                                                                 @Param("participantId") UUID participantId);

}
