package com.ss.prodsel.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class IpFilter extends OncePerRequestFilter {
  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(IpFilter.class);
  private final List<String> allowedIps;

  public IpFilter(final List<String> allowedIps) {
    this.allowedIps = List.copyOf(allowedIps);
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
                                  final HttpServletResponse response,
                                  final FilterChain filterChain)
    throws ServletException, IOException {
    if (!allowedIps.contains(request.getRemoteAddr())) {
      LOGGER.warn("Forbidden access from IP: {}", request.getRemoteAddr());
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    filterChain.doFilter(request, response);
  }
}
