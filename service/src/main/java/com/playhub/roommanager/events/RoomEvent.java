package com.playhub.roommanager.events;

import com.playhub.roommanager.model.RoomParticipants;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

@Getter
public class RoomEvent extends ApplicationEvent {

    private final DomainEventType eventType;

    private final RoomParticipants roomParticipants;

    public RoomEvent(Object source, DomainEventType eventType, RoomParticipants roomParticipants) {
        super(source);
        this.eventType = eventType;
        this.roomParticipants = roomParticipants;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RoomEvent that = (RoomEvent) object;
        return Objects.equals(source, that.source)
                && Objects.equals(eventType, that.eventType)
                && Objects.equals(roomParticipants, that.roomParticipants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, eventType, roomParticipants);
    }

}
