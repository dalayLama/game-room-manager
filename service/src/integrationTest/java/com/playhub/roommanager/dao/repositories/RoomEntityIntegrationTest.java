package com.playhub.roommanager.dao.repositories;

import com.jimbeam.test.utils.spring.jpa.TestDBFacade;
import com.jimbeam.test.utils.spring.liquibase.LiquibaseConfig;
import com.jimbeam.test.utils.testcontainer.postgres.PostgresContainer;
import com.playhub.roommanager.dao.entities.RoomParticipantEntity;
import com.playhub.roommanager.dao.entities.RoomEntity;
import com.playhub.roommanager.service.testbuilders.RoomParticipantEntityTestBuilder;
import com.playhub.roommanager.service.testbuilders.RoomEntityTestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PostgresContainer.class, LiquibaseConfig.class, TestDBFacade.Config.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ActiveProfiles("integration-test")
@Slf4j
class RoomEntityIntegrationTest {

    @Autowired
    private TestDBFacade testDBFacade;

    private final Map<UUID, Set<UUID>> roomsMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        log.info("Creating rooms");
        int roomCount = 2;
        int participantCount = 2;
        List<RoomEntity> rooms = new ArrayList<>();
        for (int i = 0; i < roomCount; i++) {
            RoomEntity room = RoomEntityTestBuilder.newRoom().build();
            IntStream.range(0, participantCount)
                    .mapToObj(j -> RoomParticipantEntityTestBuilder.newParticipant().build())
                    .forEach(room::addParticipant);
            rooms.add(room);
        }
        List<RoomEntity> savedRooms = testDBFacade.saveAll(rooms);
        savedRooms.forEach(room -> {
            Set<UUID> ids = room.getParticipants().stream().map(RoomParticipantEntity::getParticipantId).collect(Collectors.toSet());
            roomsMap.put(room.getId(), ids);
        });
        log.info("Created {} rooms", roomCount);
        log.info("------------------------------------------------------------------------------");
    }

    @AfterEach
    void clearUp() {
        log.info("Clearing database");
        testDBFacade.cleanDatabase("room_participants", "rooms");
        log.info("------------------------------------------------------------------------------");
    }

    @Test
    void addParticipant() {
        UUID roomId = roomsMap.keySet().stream().findFirst().orElseThrow();
        testDBFacade.findById(roomId, RoomEntity.class, room -> {
            RoomParticipantEntity newParticipant = RoomParticipantEntityTestBuilder.newParticipant().build();
            room.addParticipant(newParticipant);
            return testDBFacade.save(room);
        });

        Integer participantsCount = testDBFacade.findById(roomId, RoomEntity.class, room ->
                room.getParticipants().size()
        );
        assertThat(roomsMap.get(roomId).size() + 1).isEqualTo(participantsCount);
    }

    @Test
    void deleteParticipant() {
        UUID roomId = roomsMap.keySet().stream().findFirst().orElseThrow();
        UUID participantId = roomsMap.get(roomId).stream().findFirst().orElseThrow();
        testDBFacade.findById(roomId, RoomEntity.class, room -> {
            room.findParticipantById(participantId).ifPresent(room::deleteParticipant);
            return testDBFacade.save(room);
        });

        Integer participantsCount = testDBFacade.findById(roomId, RoomEntity.class, room ->
                room.getParticipants().size()
        );
        assertThat(roomsMap.get(roomId).size() - 1).isEqualTo(participantsCount);
    }

    @Test
    void deleteRoom() {
        UUID roomId = roomsMap.keySet().stream().findFirst().orElseThrow();
        testDBFacade.findById(roomId, RoomEntity.class, room -> {
            TestEntityManager testEntityManager = testDBFacade.getTestEntityManager();
            testEntityManager.remove(room);
            return room;
        });

        RoomEntity result = testDBFacade.findById(roomId, RoomEntity.class);
        assertThat(result).isNull();
    }

}