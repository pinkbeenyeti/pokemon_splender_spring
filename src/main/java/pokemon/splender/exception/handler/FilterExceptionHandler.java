package pokemon.splender.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pokemon.splender.exception.CustomFilterException;
import pokemon.splender.exception.ErrorMessage;

@Component
@RequiredArgsConstructor
public class FilterExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomFilterException ex) {
            handleException(response, ex);
        }
    }

    private void handleException(HttpServletResponse response, CustomFilterException ex)
        throws IOException {
        // 예외에서 ErrorMessage 추출
        ErrorMessage errorMessage = ex.getErrorMessage();

        // 응답 상태 코드 설정
        response.setStatus(ex.getStatus().value());

        // 문자 인코딩 설정 (UTF-8)
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // ObjectMapper를 사용하여 ErrorMessage를 JSON으로 변환
        String jsonResponse = objectMapper.writeValueAsString(errorMessage);

        // JSON 응답으로 변환
        response.getWriter().write(jsonResponse);
    }
}
