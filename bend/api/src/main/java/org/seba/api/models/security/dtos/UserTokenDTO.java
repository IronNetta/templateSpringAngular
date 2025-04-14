package org.seba.api.models.security.dtos;

public record UserTokenDTO(
        UserSessionDTO user,
        String token
) {
}

