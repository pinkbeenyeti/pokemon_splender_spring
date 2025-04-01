package pokemon.splender.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pokemon.splender.exception.CustomFilterException;
import pokemon.splender.jwt.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (isApiPath(requestURI)) {
            handleTokenAuthentication(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isApiPath(String requestURI) {
        return requestURI.startsWith("/api/");
    }

    private void handleTokenAuthentication(HttpServletRequest request,
        HttpServletResponse response) {
        String token = jwtUtil.extractAccessToken(request);

        if (jwtUtil.validateToken(token)) {
            Long userId = Long.valueOf(jwtUtil.getSubject(token));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            throw CustomFilterException.invalidTokenException();
        }
    }
}
