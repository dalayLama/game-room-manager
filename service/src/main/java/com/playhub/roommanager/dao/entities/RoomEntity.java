package com.playhub.roommanager.dao.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Entity
@Table(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString(exclude = "participants")
public class RoomEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "owner_id", nullable = false, updatable = false)
    private UUID ownerId;

    @Column(name = "code", updatable = false)
    @Size(min = 4, max = 4)
    private String securityCode;

    @Column(name = "max_participants", updatable = false)
    @Min(1)
    private Integer maxParticipants;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Fetch(FetchMode.SUBSELECT)
    private List<RoomParticipantEntity> participants = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp(source = SourceType.VM)
    private Instant createdAt;

    public void addParticipant(RoomParticipantEntity participant) {
        participant.setRoom(this);
        participants.add(participant);
    }

    public void deleteParticipant(RoomParticipantEntity participant) {
        participants.remove(participant);
        participant.setId(null);
    }

    public Optional<RoomParticipantEntity> findParticipantById(UUID id) {
        return findParticipant(p -> p.getParticipantId().equals(id));
    }

    public Optional<RoomParticipantEntity> findParticipant(Predicate<RoomParticipantEntity> predicate) {
        return participants.stream().filter(predicate).findFirst();
    }

    public List<RoomParticipantEntity> findParticipants(Predicate<RoomParticipantEntity> predicate) {
        return participants.stream().filter(predicate).toList();
    }

}
