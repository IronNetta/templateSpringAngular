package org.seba.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.api.models.security.dtos.UserSessionDTO;
import org.seba.api.models.security.dtos.UserTokenDTO;
import org.seba.api.models.security.forms.LoginForm;
import org.seba.api.models.security.forms.RegisterForm;
import org.seba.entities.User;
import org.seba.services.security.AuthService;
import org.seba.utils.jwt.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private org.seba.api.controllers.auth.AuthController authController;

    private MockMvc mockMvc;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();
        
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
    }

    @Test
    void register_ShouldReturnNoContent() throws Exception {
        // Given
        RegisterForm registerForm = new RegisterForm("newuser", "newuser@example.com", "password");

        ObjectMapper objectMapper = new ObjectMapper();
        String registerFormJson = objectMapper.writeValueAsString(registerForm);

        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerFormJson))
                .andExpect(status().isNoContent());

        verify(authService).register(any(User.class));
    }

    @Test
    void login_ShouldReturnUserTokenDTO() throws Exception {
        // Given
        LoginForm loginForm = new LoginForm("test@example.com", "password");
        
        when(authService.login(eq("test@example.com"), eq("password"))).thenReturn(mockUser);
        when(jwtUtil.generateToken(mockUser)).thenReturn("test-jwt-token");

        UserSessionDTO userSessionDTO = UserSessionDTO.fromUser(mockUser);
        UserTokenDTO userTokenDTO = new UserTokenDTO(userSessionDTO, "test-jwt-token");

        ObjectMapper objectMapper = new ObjectMapper();
        String loginFormJson = objectMapper.writeValueAsString(loginForm);

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginFormJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userName").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("test-jwt-token"));

        verify(authService).login(eq("test@example.com"), eq("password"));
        verify(jwtUtil).generateToken(mockUser);
    }
}