package telegrambotchatgpt.controllers.authorization;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telegrambotchatgpt.dto.authorization.AuthenticationRequest;
import telegrambotchatgpt.dto.jwt.AuthenticationResponse;
import telegrambotchatgpt.service.AuthorizationService;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/v1/authorization/login")
@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return authorizationService.login(authenticationRequest);
    }
}
