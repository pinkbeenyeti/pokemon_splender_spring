package pokemon.splender.auth.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pokemon.splender.auth.service.AuthService;
import pokemon.splender.response.CustomResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/token/refresh")
    public ResponseEntity<Void> refreshToken(
        @CookieValue("refresh_token") String refreshToken,
        HttpServletResponse response
    ) {
        authService.reissueToken(refreshToken, response);
        return CustomResponse.noContent();
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(
        @CookieValue("refresh_token") String refreshToken,
        HttpServletResponse response
    ) {
        authService.logout(refreshToken, response);
        return CustomResponse.noContent();
    }

}
