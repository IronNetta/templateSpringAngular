package org.seba.api.models.security.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.seba.entities.User;

public record RegisterForm(
        @NotBlank @Size(max = 50)
        String username,
        @NotBlank @Size(max = 150)
        String email,
        @NotBlank
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
