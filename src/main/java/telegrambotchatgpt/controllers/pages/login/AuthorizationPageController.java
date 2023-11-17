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

    @PostMapping("/reset-password/{username}")
    public ResponseEntity<String> sendResetPasswordMessage(@PathVariable String username) {
        return authorizationService.sendResetPasswordMessage(username);
    }

    @GetMapping("/reset-password/verify")
    public ResponseEntity<String> verifyResetPassword(@RequestParam String username, @RequestParam String token){
        return authorizationService.verifyResetPassword(username, token);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid RegistrationRequest registrationRequest){
        return authorizationService.resetPassword(registrationRequest);
    }
}
