package com.playhub.roommanager.controllers;

import com.playhub.autoconfigure.security.rest.secutiry.PlayHubUser;
import com.playhub.roommanager.mappers.RoomMapper;
import com.playhub.roommanager.model.RoomParticipants;
import com.playhub.roommanager.model.requests.NewParticipantRequest;
import com.playhub.roommanager.model.requests.NewRoomRequest;
import com.playhub.roommanager.restapi.ApiPaths;
import com.playhub.roommanager.restapi.dto.requests.NewParticipantRequestDto;
import com.playhub.roommanager.restapi.dto.requests.NewRoomRequestDto;
import com.playhub.roommanager.restapi.dto.responses.RoomParticipantsDto;
import com.playhub.roommanager.services.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomService roomService;

    private final RoomMapper mapper;

    @PostMapping(ApiPaths.V1_ROOMS)
    public ResponseEntity<RoomParticipantsDto> createRoom(
            @AuthenticationPrincipal PlayHubUser user,
            @Valid @RequestBody NewRoomRequestDto requestDto) {
        NewRoomRequest request = mapper.toRequest(user.getId(), requestDto);
        RoomParticipants roomParticipants = roomService.createRoom(request);
        RoomParticipantsDto responseDto = mapper.toDto(roomParticipants);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(responseDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @DeleteMapping(ApiPaths.V1_ROOM)
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") UUID roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(ApiPaths.V1_ROOM_PARTICIPANTS)
    public ResponseEntity<Void> addParticipant(@PathVariable("roomId") UUID roomId,
                                               @Valid @RequestBody NewParticipantRequestDto requestDto) {
        NewParticipantRequest request = mapper.toNewParticipantRequest(requestDto);
        roomService.addParticipant(roomId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(ApiPaths.V1_ROOM_PARTICIPANT)
    public ResponseEntity<Void> deleteParticipant(
            @PathVariable("roomId") UUID roomId,
            @PathVariable("participantId") UUID participantId) {
        roomService.deleteParticipant(roomId, participantId);
        return ResponseEntity.ok().build();
    }

}
