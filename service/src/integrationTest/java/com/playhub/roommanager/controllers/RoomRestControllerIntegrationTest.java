package com.playhub.roommanager.controllers;

import com.jimbeam.test.utils.spring.liquibase.LiquibaseConfig;
import com.jimbeam.test.utils.testcontainer.postgres.PostgresContainer;
import com.playhub.roommanager.App;
import com.playhub.roommanager.restapi.ApiPaths;
import com.playhub.roommanager.userinfo.UserInfo;
import com.playhub.roommanager.userinfo.UserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
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


}