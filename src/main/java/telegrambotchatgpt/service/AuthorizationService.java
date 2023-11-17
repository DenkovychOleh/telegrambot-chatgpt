package telegrambotchatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telegrambotchatgpt.dto.authorization.request.LoginRequest;
import telegrambotchatgpt.dto.authorization.request.RegistrationRequest;
import telegrambotchatgpt.dto.jwt.AuthenticationResponse;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.exceptions.AuthorizationCustomException;
import telegrambotchatgpt.exceptions.JwtCustomException;
import telegrambotchatgpt.service.jwt.JwtService;
import telegrambotchatgpt.service.repositories.AppUserService;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final AppUserService appUserService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TelegramBotService telegramBotService;


    @Value("${base.url}")
    private String baseUrl;

    @Value("${reset-password.url}")
    private String resetPasswordUrl;

    @Value("${telegram.bot.title}")
    private String botTitle;


    @Transactional
    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {
        AppUser appUser = authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(appUser);
        String refreshToken = jwtService.generateRefreshToken(appUser);
        appUser.setRefreshToken(refreshToken);
        appUserService.save(appUser);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .token(jwtToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

    @Transactional
    public ResponseEntity<String> register(RegistrationRequest registrationRequest) {
        AppUser appUser = validateRegistrationRequest(registrationRequest);
        appUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        return ResponseEntity.ok("User has been registered. You can log in!");
    }

    @Transactional
    public ResponseEntity<String> sendResetPasswordMessage(String username) {
        AppUser appUser = appUserService.findByUsername(username);
        String refreshToken = jwtService.generateRefreshToken(appUser);
        appUser.setRefreshToken(refreshToken);
        appUserService.save(appUser);
        String resetPasswordLink = String.format(
                "%s%sverify?username=%s&token=%s",
                baseUrl, resetPasswordUrl, appUser.getUsername(), appUser.getRefreshToken()
        );
        String author = botTitle;
        telegramBotService.processTextMessageByAppUser(appUser, author, resetPasswordLink);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<String> verifyResetPassword(String username, String token) {
        AppUser appUser = appUserService.findByUsername(username);
        if (appUser.getRefreshToken().equals(token) && !jwtService.isTokenExpired(token)) {
            return ResponseEntity.ok("Account has verified");
        } else {
            throw new JwtCustomException("Token has expired. Please regenerate token and try again");
        }
    }

    @Transactional
    public ResponseEntity<String> resetPassword(RegistrationRequest registrationRequest) {
        AppUser appUser = appUserService.findByUsername(registrationRequest.getUsername());
        appUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        appUserService.save(appUser);
        String message = "The password has been updated";
        String author = botTitle;
        telegramBotService.processTextMessageByAppUser(appUser, author, message);
        return ResponseEntity.ok(message);
    }


    protected AppUser authenticate(LoginRequest loginRequest) {
        AppUser appUser = appUserService.findByUsername(loginRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            return appUser;
        } catch (Exception e) {
            throw new AuthorizationCustomException();
        }
    }


    private AppUser validateRegistrationRequest(RegistrationRequest registrationRequest) {
        AppUser appUser = appUserService.findByUsername(registrationRequest.getUsername());
        if (appUser.getPassword() != null) {
            throw new AuthorizationCustomException("A user with this username is already registered");
        }
        return appUser;
    }
}
