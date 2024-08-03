package com.playhub.roommanager.restapi;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPaths {

    public static final String V1_ROOMS = "/api/v1/rooms";

    public static final String V1_ROOM = "/api/v1/rooms/{roomId}";

    public static final String V1_ROOM_PARTICIPANTS = "/api/v1/rooms/{roomId}/participants";

    public static final String V1_ROOM_PARTICIPANT = "/api/v1/rooms/{roomId}/participants/{participantId}";

}
