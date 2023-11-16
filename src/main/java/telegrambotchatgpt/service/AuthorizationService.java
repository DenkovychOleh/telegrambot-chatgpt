package telegrambotchatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telegrambotchatgpt.dto.authorization.AuthenticationRequest;
import telegrambotchatgpt.dto.jwt.AuthenticationResponse;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.exceptions.AuthorizationCustomException;
import telegrambotchatgpt.service.jwt.JwtService;
import telegrambotchatgpt.service.repositories.AppUserService;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final AppUserService appUserService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest authenticationRequest) {
        AppUser appUser = authenticate(authenticationRequest);
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


    protected AppUser authenticate(AuthenticationRequest authenticationRequest) {
        AppUser appUser = appUserService.findByUsername(authenticationRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            return appUser;
        } catch (Exception e) {
            throw new AuthorizationCustomException();
        }
    }
}
