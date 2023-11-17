package com.amido.stacks.workloads;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/** ApplicationConfig - Configuration class for Auth0 application security. */
@Configuration("MySecurityConfig")
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationConfig {

  private static final String V1_MENU_ENDPOINT = "/v1/menu";
  private static final String V1_MENU = "/v1/menu/**";
  private static final String V2_MENU_ENDPOINT = "/v2/menu";
  private static final String V2_MENU = "/v2/menu/**";

  @Value(value = "${auth.apiAudience}")
  private String apiAudience;

  @Value(value = "${auth.issuer}")
  private String issuer;

  @Value(value = "${auth.isEnabled}")
  private boolean isEnabled;

  /**
   * Provide CorsConfiguration for each request
   *
   * @return
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
    configuration.setAllowCredentials(true);
    configuration.addAllowedHeader("Authorization");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Provides a custom {@link ObjectMapper} that ignores extra fields.
   *
   * @return the modified implementation
   */
  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * Configure your API to use the RS256 and protect API endpoints. Config switch is put in place to
   * enable or disable Auth (isEnabled)
   *
   * <p>/api/public: available for non-authenticated requests. /api/private: available for
   * authenticated requests containing an Access-Token with no additional scopes.
   *
   * @param http
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults());
    if (BooleanUtils.isTrue(isEnabled)) {
      return enableAuth(http);
    }
    return permitAll(http);
  }

  /**
   * authenticated() - endpoints are protected
   *
   * @param http
   * @throws Exception
   */
  private SecurityFilterChain enableAuth(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authConfig -> authConfig.requestMatchers(V1_MENU_ENDPOINT)
        .authenticated()
        .requestMatchers(V2_MENU_ENDPOINT)
        .authenticated()
        .requestMatchers(V1_MENU)
        .authenticated()
        .requestMatchers(V2_MENU)
        .authenticated()).build();
  }

  /**
   * permitAll() - endpoints are not protected
   *
   * @param http
   * @throws Exception
   */
  private SecurityFilterChain permitAll(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authConfig -> authConfig.requestMatchers("/**").permitAll())
        .httpBasic(Customizer.withDefaults()).build();
  }
}
