package com.amido.stacks.core.api.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config.correlation")
public class CorrelationIdFilterConfiguration {

  public static final String DEFAULT_CORRELATION_REQUEST_HEADER = "x-correlation-id";
  public static final String DEFAULT_RESPONSE_TOKEN_HEADER = "x-correlation-id";
  public static final String DEFAULT_MDC_UUID_TOKEN_KEY = "CorrelationId";

  private String requestHeader = DEFAULT_CORRELATION_REQUEST_HEADER;
  private String responseHeader = DEFAULT_RESPONSE_TOKEN_HEADER;
  private String logTokenKey = DEFAULT_MDC_UUID_TOKEN_KEY;

  @Bean
  public FilterRegistrationBean<CorrelationIdFilter> servletRegistrationBean() {

    final FilterRegistrationBean<CorrelationIdFilter> registrationBean =
        new FilterRegistrationBean<>();

    final CorrelationIdFilter logFilter =
        new CorrelationIdFilter(responseHeader, logTokenKey, requestHeader);

    registrationBean.setFilter(logFilter);
    return registrationBean;
  }
}
