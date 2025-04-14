package org.seba.services.security.impl;

import org.seba.entities.User;
import org.seba.enums.UserRole;
import org.seba.exceptions.user.BadCredentialsException;
import org.seba.exceptions.user.UserNotFoundException;
import org.seba.repositories.UserRepository;
import org.seba.services.security.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserNotFoundException(HttpStatus.NOT_ACCEPTABLE, "Bad credentials");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);

        userRepository.save(user);
    }


    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User with email " + email + " not found")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(HttpStatus.NOT_ACCEPTABLE, "Bad credentials");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, email)
        );
    }
}