package org.seba.api.controllers.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.seba.api.models.security.dtos.UserSessionDTO;
import org.seba.api.models.security.dtos.UserTokenDTO;
import org.seba.api.models.security.forms.LoginForm;
import org.seba.api.models.security.forms.RegisterForm;
import org.seba.entities.User;
import org.seba.services.security.AuthService;
import org.seba.utils.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints pour l'authentification des utilisateurs")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Inscription d'un utilisateur", description = "Permet à un utilisateur de s'inscrire.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur enregistré avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterForm form) {
        authService.register(form.toUser());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Connexion d'un utilisateur", description = "Authentifie un utilisateur et retourne un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants incorrects")
    })
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(@Valid @RequestBody LoginForm form) {
        User user = authService.login(form.email(), form.password());
        UserSessionDTO userDTO = UserSessionDTO.fromUser(user);
        String token = jwtUtil.generateToken(user);
        UserTokenDTO userTokenDTO = null;
        if (token != null) {
            userTokenDTO = new UserTokenDTO(userDTO, token);
        }
        return ResponseEntity.ok(userTokenDTO);
    }
}
