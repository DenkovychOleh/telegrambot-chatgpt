package telegrambotchatgpt.service.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telegrambotchatgpt.dto.jwt.AuthenticationResponse;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.exceptions.JwtCustomException;
import telegrambotchatgpt.service.repositories.AppUserService;

@RequiredArgsConstructor
@Service
public class TokenRefreshService {

    private final JwtService jwtService;
    private final AppUserService appUserService;


    public AuthenticationResponse refreshTokens(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);
            AppUser appUser = appUserService.findByUsername(username);

            if (appUser.getRefreshToken().equals(refreshToken)) {
                String newAccessToken = jwtService.generateToken(appUser);
                String newRefreshToken = jwtService.generateRefreshToken(appUser);
                appUser.setRefreshToken(newRefreshToken);
                appUserService.save(appUser);

                return AuthenticationResponse.builder()
                        .token(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
            } else {
                throw new JwtCustomException("Invalid refresh token");
            }
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Refresh token has expired", e.getClaims().getExpiration().toInstant());
        }
    }
}
