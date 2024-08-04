package com.playhub.roommanager.events;

import com.playhub.roommanager.model.RoomParticipants;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

@Getter
public class RoomCreatedEvent extends ApplicationEvent {

    private final RoomParticipants roomParticipants;

    public RoomCreatedEvent(Object source, RoomParticipants roomParticipants) {
        super(source);
        this.roomParticipants = roomParticipants;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RoomCreatedEvent that = (RoomCreatedEvent) object;
        return source.equals(that.source) && Objects.equals(roomParticipants, that.roomParticipants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, roomParticipants);
    }

}
