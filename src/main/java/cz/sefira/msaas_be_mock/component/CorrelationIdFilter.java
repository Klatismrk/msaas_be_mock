package cz.sefira.msaas_be_mock.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorrelationIdFilter implements Filter {

  public static final String HEADER_NAME = "X-Correlation-Id";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String correlationId = req.getHeader(HEADER_NAME);

    // přidá do response
    res.setHeader(HEADER_NAME, correlationId);

    // pokračuje v request chainu
    chain.doFilter(request, response);
  }
}
