package org.seba.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.api.models.CustomPage;
import org.seba.api.models.security.dtos.UserSessionDTO;
import org.seba.api.models.user.forms.UserForm;
import org.seba.entities.User;
import org.seba.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User mockUser;
    private UserSessionDTO userSessionDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
        
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        userSessionDTO = UserSessionDTO.fromUser(mockUser);
    }

    @Test
    void getAllUsers_ShouldReturnCustomPage() throws Exception {
        // Given
        List<User> users = List.of(mockUser);
        Page<User> userPage = new PageImpl<>(users);
        when(userService.getUsers(any(), any(PageRequest.class))).thenReturn(userPage);

        // When & Then
        mockMvc.perform(get("/user")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(1))
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(userService).getUsers(any(), any(PageRequest.class));
    }

    @Test
    void getUserByEmail_ShouldReturnUserSessionDTO() throws Exception {
        // Given
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(get("/user/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("test@example.com"));

        verify(userService).getUserByEmail("test@example.com");
    }

    @Test
    void createUser_ShouldReturnUserSessionDTO() throws Exception {
        // Given
        UserForm userForm = new UserForm("newuser", "newuser@example.com", "password");

        when(userService.saveUser(any(User.class))).thenReturn(mockUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String userFormJson = objectMapper.writeValueAsString(userForm);

        // When & Then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userFormJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("test@example.com"));

        verify(userService).saveUser(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUserSessionDTO() throws Exception {
        // Given
        UserForm userForm = new UserForm("updateduser", "updated@example.com", "newpassword");

        when(userService.updateUser(any(User.class), eq("test@example.com"))).thenReturn(mockUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String userFormJson = objectMapper.writeValueAsString(userForm);

        // When & Then
        mockMvc.perform(put("/user/{email}", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userFormJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("test@example.com"));

        verify(userService).updateUser(any(User.class), eq("test@example.com"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        // Given
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(delete("/user/{email}", "test@example.com"))
                .andExpect(status().isNoContent());

        verify(userService).getUserByEmail("test@example.com");
        verify(userService).deleteUser(eq(1L));
    }
}