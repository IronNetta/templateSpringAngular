package org.seba.services.security;

import org.seba.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    void register(User user);

    User login(String email, String password);
}