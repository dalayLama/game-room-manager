package com.playhub.roommanager.userinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

@UtilityClass
public class UserInfoUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static UserInfo getUserInfo() {
        ClassPathResource resource = new ClassPathResource("user/user-info.json");
        return objectMapper.readValue(resource.getFile(), UserInfo.class);
    }

}
