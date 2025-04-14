package org.seba.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.seba.api.models.CustomPage;
import org.seba.api.models.security.dtos.UserSessionDTO;
import org.seba.api.models.user.forms.UserForm;
import org.seba.entities.User;
import org.seba.requests.SearchParam;
import org.seba.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomPage<UserSessionDTO>> getAllUsers(
            @RequestParam(required = false) Map<String, String> params,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "username") String sort

    ) {
        List<SearchParam<User>> searchParams = SearchParam.create(params);
        Page<User> users = userService.getUsers(
                searchParams,
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );
        List<UserSessionDTO> dtos = users.getContent().stream()
                .map(UserSessionDTO::fromUser)
                .toList();
        CustomPage<UserSessionDTO> result = new CustomPage<>(dtos,users.getTotalPages(),users.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserSessionDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserSessionDTO.fromUser(user));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserSessionDTO> createUser(@RequestBody UserForm user) {
        User savedUser = userService.saveUser(user.toUser());
        return ResponseEntity.ok(UserSessionDTO.fromUser(savedUser));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{email}")
    public ResponseEntity<UserSessionDTO> updateUser(@PathVariable String email, @RequestBody UserForm user) {
        User updatedUser = userService.updateUser(user.toUser(), email);
        return ResponseEntity.ok(UserSessionDTO.fromUser(updatedUser));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        Long id = userService.getUserByEmail(email).getId();
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}