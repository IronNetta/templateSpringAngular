package org.seba.api.models.security.dtos;


import org.seba.entities.User;
import org.seba.enums.UserRole;

public record UserSessionDTO(
        Long id,
        UserRole role,
        String userName
) {

    public static UserSessionDTO fromUser(User user) {
        return new UserSessionDTO(user.getId(),user.getRole(),user.getUsername());
    }
}
