package pokemon.splender.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pokemon.splender.exception.handler.FilterExceptionHandler;
import pokemon.splender.jwt.JwtAuthenticationFilter;
import pokemon.splender.oauth.OAuth2AuthenticationSuccessHandler;
import pokemon.splender.oauth.OAuth2UserService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final FilterExceptionHandler filterExceptionHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2UserService oAuth2UserService;

    // Security에 필터 적용
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // cors 설정
            .csrf(AbstractHttpConfigurer::disable) // csrf -> csrf.disable(), csrf 비활성화
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))  // JWT 인증 방식에서는 세션 미사용)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("api/**").authenticated()
                    .anyRequest()
                    .permitAll()) // FilterSecurityInterceptor 필터 적용, 'api/'가 붙은 요청은 인증 필터들 실행, 그 외 모든 요청에 대한 허가
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2UserService)
                )
                .successHandler(oAuth2AuthenticationSuccessHandler))
            .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class) // JWT 인증을 처리하는 필터 적용
            .addFilterBefore(filterExceptionHandler,
                JwtAuthenticationFilter.class); // 필터에서 발생하는 예외를 처리하는 필터 적용

        return http.build();
    }

    // cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(
            List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
