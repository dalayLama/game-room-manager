package com.playhub.roommanager.controllers;

import com.jimbeam.test.utils.spring.liquibase.LiquibaseConfig;
import com.jimbeam.test.utils.testcontainer.postgres.PostgresContainer;
import com.playhub.roommanager.App;
import com.playhub.roommanager.exceptions.ErrorType;
import com.playhub.roommanager.restapi.ApiPaths;
import com.playhub.roommanager.userinfo.UserInfo;
import com.playhub.roommanager.userinfo.UserInfoUtils;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = {App.class, PostgresContainer.class, LiquibaseConfig.class}
)
@ActiveProfiles({"integration-test"})
@AutoConfigureMockMvc
@Slf4j
class RoomRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldCreateNewRoom() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();

        mockMvc.perform(post(ApiPaths.V1_ROOMS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "maxParticipants": 10,
                                    "securityCode": 5555,
                                    "participants": ["%s"]
                                }
                                """.formatted(userInfo.id())
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.ownerId").value(userInfo.id().toString()))
                .andExpect(jsonPath("$.maxParticipants").value(10))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.participants[0]").value(userInfo.id().toString()))
                .andDo(print());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "EN-en, ''",
            "EN-en, 55555",
            "RU-ru, ''",
            "RU-ru, 55555"
    })
    void shouldReturn400IfRequestIsNotValid(Locale locale, String securityCode) throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();

        String securityCodeMessage = "The room password must consist of 4 characters";
        String maxParticipantsMessage = "The maximum number of participants cannot be less than 1 in the room";
        if (locale.getLanguage().equals("ru-ru")) {
            securityCodeMessage = "Пароль комнаты должен состоять из 4 символов";
            maxParticipantsMessage = "Максимальное количество участников не может быть меньше 1 в комнате";
        }
        mockMvc.perform(post(ApiPaths.V1_ROOMS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "maxParticipants": 0,
                                    "securityCode": "%s",
                                    "participants": ["%s"]
                                }
                                """.formatted(securityCode, userInfo.id())
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationDetails.securityCode.[0]").value(securityCodeMessage))
                .andExpect(jsonPath("$.validationDetails.maxParticipants.[0]").value(maxParticipantsMessage))
                .andDo(print());
    }

    @Test
    void shouldReturn415IfContentTypeIsNotDeclared() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();

        mockMvc.perform(post(ApiPaths.V1_ROOMS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .content("""
                                {
                                    "maxParticipants": 10,
                                    "securityCode": 5555,
                                    "participants": ["%s"]
                                }
                                """.formatted(userInfo.id())
                        ))
                .andExpect(status().isUnsupportedMediaType())
                .andDo(print());
    }

    @Test
    void shouldReturn401IfBearerTokenIsNotDeclared() throws Exception {
        mockMvc.perform(post(ApiPaths.V1_ROOMS)
                        .content("""
                                {
                                    "maxParticipants": 10,
                                    "securityCode": 5555,
                                    "participants": ["%s"]
                                }
                                """
                        ))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldAddParticipant() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("638f2195-ee04-48cd-ada0-90f6711f0cad");

        mockMvc.perform(put(ApiPaths.V1_ROOM_PARTICIPANTS, roomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "participantId": "c57322b3-aea9-402f-bf7a-b845d12c2009",
                                    "securityCode": "5555"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldNotAddExistedParticipant() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("638f2195-ee04-48cd-ada0-90f6711f0cad");

        mockMvc.perform(put(ApiPaths.V1_ROOM_PARTICIPANTS, roomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "participantId": "b2b27a52-24bd-41df-8037-6ea9150d7945",
                                    "securityCode": "5555"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EN-en", "RU-ru"})
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn403IfSecuredCodeIsNotValid(Locale locale) throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("638f2195-ee04-48cd-ada0-90f6711f0cad");
        String title = localedMessage(locale,
                "Security code is invalid",
                "Неверный пароль комнаты"
        );
        String detail = localedMessage(locale,
                "Security code of the room is invalid",
                "Пароль комнаты указан неверно");

        mockMvc.perform(put(ApiPaths.V1_ROOM_PARTICIPANTS, roomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "participantId": "b7d70113-f883-4b15-bdd2-2a105e137f1f",
                                    "securityCode": "6666"
                                }
                                """
                        ))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorType.SECURITY_CODE_INVALID.getErrorCode()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.detail").value(detail))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EN-en", "RU-ru"})
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn403IfRoomIsFull(Locale locale) throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("ec92ac5d-536b-486a-8e35-21eeca1e877b");
        String title = localedMessage(locale,
                "Room capacity exceeded",
                "Вместимость комнаты превышена"
        );
        String detail = localedMessage(locale,
                "Room capacity (current=2, max=2) exceeded",
                "Вместимость комнаты (в комнате=2, макс.=2) превышена");

        mockMvc.perform(put(ApiPaths.V1_ROOM_PARTICIPANTS, roomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "participantId": "579a1349-654e-4ee7-af08-a8a62f56481a",
                                    "securityCode": null
                                }
                                """
                        ))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorType.ROOM_CAPACITY_EXCEEDED.getErrorCode()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.detail").value(detail))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EN-en", "RU-ru"})
    void shouldReturn404IfRoomIsNotFound(Locale locale) throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("7f33a600-313e-43a2-a051-f75ae2973789");
        String title = localedMessage(locale,
                "Room not found",
                "Комната не найдена"
        );
        String detail = localedMessage(locale,
                "Room by id %s has not been found".formatted(roomId),
                "Комната с идентификатором %s не найдена".formatted(roomId));

        mockMvc.perform(put(ApiPaths.V1_ROOM_PARTICIPANTS, roomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "participantId": "579a1349-654e-4ee7-af08-a8a62f56481a",
                                    "securityCode": null
                                }
                                """
                        ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorType.ROOM_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.detail").value(detail))
                .andDo(print());
    }

    @Test
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteParticipant() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("a666cd6b-119f-4cd6-9ffe-3c820496cbeb");

        mockMvc.perform(delete(ApiPaths.V1_ROOM_PARTICIPANT, roomId, userInfo.id())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteRoomDuringDeletingParticipantIfParticipantIsOwner() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("364c0c8b-2c64-4de7-a3cf-adc5d7a4df11");

        mockMvc.perform(delete(ApiPaths.V1_ROOM_PARTICIPANT, roomId, userInfo.id())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Sql(scripts = {
            "/sql/fill-rooms.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "delete from room_participants",
            "delete from rooms"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteRoom() throws Exception {
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        UUID roomId = UUID.fromString("364c0c8b-2c64-4de7-a3cf-adc5d7a4df11");

        mockMvc.perform(delete(ApiPaths.V1_ROOM, roomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userInfo.jwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    private String localedMessage(Locale locale, String en, String ru) {
        return locale.getLanguage().equals("ru-ru") ? ru : en;
    }


}