package org.seba.api.models.user.forms;

import org.seba.entities.User;

public record UserForm(
        String username,
        String email,
        String password
) {
    public User toUser() {
        return new User(
                username,
                email,
                password
        );
    }
}
