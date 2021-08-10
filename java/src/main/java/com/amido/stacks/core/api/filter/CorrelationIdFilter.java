package com.amido.stacks.core.api.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(1)
public class CorrelationIdFilter extends OncePerRequestFilter {

  private final String responseHeader;
  private final String mdcTokenKey;
  private final String requestHeader;

  public CorrelationIdFilter() {
    responseHeader = CorrelationIdFilterConfiguration.DEFAULT_RESPONSE_TOKEN_HEADER;
    mdcTokenKey = CorrelationIdFilterConfiguration.DEFAULT_MDC_UUID_TOKEN_KEY;
    requestHeader = CorrelationIdFilterConfiguration.DEFAULT_CORRELATION_REQUEST_HEADER;
  }

  public CorrelationIdFilter(String responseHeader, String mdcTokenKey, String requestHeader) {
    this.responseHeader = responseHeader;
    this.mdcTokenKey = mdcTokenKey;
    this.requestHeader = requestHeader;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      final String correlationId = UUID.randomUUID().toString();
      MDC.put(mdcTokenKey, correlationId);

      if (!StringUtils.isEmpty(responseHeader)) {
        httpServletResponse.addHeader(responseHeader, correlationId);
      }

      httpServletRequest.setAttribute(mdcTokenKey, correlationId);
      filterChain.doFilter(httpServletRequest, httpServletResponse);

    } finally {
      MDC.remove(mdcTokenKey);
    }
  }
}
