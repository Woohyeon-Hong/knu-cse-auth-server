package kr.ac.knu.cse.infrastructure.security.support;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.ac.knu.cse.global.exception.BusinessException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Configuration
public class FilterBusinessExceptionWriter {

    public void write(
            HttpServletResponse response,
            BusinessException e
    ) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String body = """
        {
          "code": "%s",
          "message": "%s"
        }
        """.formatted(e.getCode(), e.getMessage());

        response.getWriter().write(body);
    }
}
