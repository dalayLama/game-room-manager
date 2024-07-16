package com.playhub.roommanager.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "room_participants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomParticipantEntity {

    public static RoomParticipantEntity newParticipant(UUID participantId) {
        return builder()
                .id(new RoomParticipantId(null, participantId))
                .build();
    }

    @EmbeddedId
    private RoomParticipantId id;

    @CreationTimestamp(source = SourceType.VM)
    @Column(name = "added_at", updatable = false)
    private Instant addedAt;

    public UUID getParticipantId() {
        return id.getParticipantId();
    }

    public RoomEntity getRoom() {
        return this.id.getRoom();
    }

    public void setRoom(RoomEntity room) {
        this.id.setRoom(room);
    }

    @Override
    public String toString() {
        return "ParticipantEntity{" +
                "participantId=" + id +
                ", addedAt=" + addedAt +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RoomParticipantEntity that = (RoomParticipantEntity) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
