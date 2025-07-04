package org.seba.services.user.impl;

import lombok.RequiredArgsConstructor;
import org.seba.entities.User;
import org.seba.exceptions.user.UserAlreadyExistExeption;
import org.seba.exceptions.user.UserNotFoundException;
import org.seba.repositories.UserRepository;
import org.seba.requests.SearchParam;
import org.seba.specifications.SearchSpecification;
import org.seba.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getUsers(List<SearchParam<User>> searchParams, Pageable pageable) {
        if (searchParams.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistExeption(HttpStatus.CONFLICT, "User already exists with email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found with id: " + user.getId()));

        if (!existingUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistExeption(HttpStatus.CONFLICT, "Email already in use: " + user.getEmail());
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }
}
