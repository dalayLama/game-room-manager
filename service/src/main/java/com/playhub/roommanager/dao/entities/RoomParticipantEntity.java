package com.playhub.roommanager.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "room_participants", indexes = {
        @Index(name = "room_participant_uindex", columnList = "room_id, participant_id", unique = true),
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString(exclude = "room")
public class RoomParticipantEntity {

    public static RoomParticipantEntity newParticipant(UUID participantId) {
        return builder()
                .id(null)
                .participantId(participantId)
                .build();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    @NotNull
    private RoomEntity room;

    @Column(name = "participant_id", nullable = false, updatable = false)
    @NotNull
    private UUID participantId;

    @CreationTimestamp(source = SourceType.VM)
    @Column(name = "added_at", updatable = false)
    private Instant addedAt;

}
