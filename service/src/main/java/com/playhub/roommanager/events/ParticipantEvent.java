package com.playhub.roommanager.events;

import com.playhub.roommanager.model.Room;
import com.playhub.roommanager.model.RoomParticipant;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

@Getter
public class ParticipantEvent extends ApplicationEvent {

    private final DomainEventType eventType;

    private final Room room;

    private final RoomParticipant participant;

    public ParticipantEvent(Object source, DomainEventType eventType, Room room, RoomParticipant participant) {
        super(source);
        this.eventType = eventType;
        this.room = room;
        this.participant = participant;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ParticipantEvent event = (ParticipantEvent) object;
        return Objects.equals(source, event.source)
                && Objects.equals(eventType, event.eventType)
                && Objects.equals(room, event.room)
                && Objects.equals(participant, event.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, eventType, room, participant);
    }
}
