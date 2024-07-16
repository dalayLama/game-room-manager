package com.playhub.roommanager.userinfo;

import java.util.Set;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String name,
        Set<String> roles,
        String jwtToken
) {
}
