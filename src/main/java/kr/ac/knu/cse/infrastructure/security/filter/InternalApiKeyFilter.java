package kr.ac.knu.cse.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.ac.knu.cse.global.exception.BusinessException;
import kr.ac.knu.cse.global.exception.internal.InternalApiForbiddenException;
import kr.ac.knu.cse.infrastructure.security.config.InternalApiProperties;
import kr.ac.knu.cse.infrastructure.security.support.FilterBusinessExceptionWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@RequiredArgsConstructor
public class InternalApiKeyFilter extends OncePerRequestFilter {

    private static final String INTERNAL_PATH = "/internal";
    private static final String HEADER_NAME = "X-Internal-Api-Key";

    private final InternalApiProperties properties;
    private final FilterBusinessExceptionWriter filterBusinessExceptionWriter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.startsWith(INTERNAL_PATH)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String apiKey = request.getHeader(HEADER_NAME);
            validateApiKey(apiKey);

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            filterBusinessExceptionWriter.write(response, e);
        }
    }

    private void validateApiKey(String apiKey) {
        if (!properties.apiKey().equals(apiKey)) {
            throw new InternalApiForbiddenException();
        }
    }

}
