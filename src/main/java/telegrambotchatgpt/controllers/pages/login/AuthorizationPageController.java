package telegrambotchatgpt.controllers.pages.login;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telegrambotchatgpt.dto.authorization.request.LoginRequest;
import telegrambotchatgpt.dto.authorization.request.RegistrationRequest;
import telegrambotchatgpt.dto.jwt.AuthenticationResponse;
import telegrambotchatgpt.service.AuthorizationService;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/v1/authorization")
@RestController
public class AuthorizationPageController {

    private final AuthorizationService authorizationService;


    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return authorizationService.register(registrationRequest);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return authorizationService.login(loginRequest);
    }
}
