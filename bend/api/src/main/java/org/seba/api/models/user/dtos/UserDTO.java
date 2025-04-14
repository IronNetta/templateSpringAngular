package org.seba.api.models.user.dtos;


import org.seba.entities.User;

public record UserDTO(
        Long id,
        String username
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }
}