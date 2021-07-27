package com.xxAMIDOxx.xxSTACKSxx;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import java.util.Arrays;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/** ApplicationConfig - Configuration class for Auth0 application security. */
@Configuration("MySecurityConfig")
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

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
   * Configure your API to use the RS256 and protect API endpoints. Config switch is put in place to
   * enable or disable Auth (isEnabled)
   *
   * <p>/api/public: available for non-authenticated requests. /api/private: available for
   * authenticated requests containing an Access-Token with no additional scopes.
   *
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors();
    if (BooleanUtils.isTrue(isEnabled)) {
      enableAuth(http);
    } else {
      permitAll(http);
    }
  }

  /**
   * authenticated() - endpoints are protected
   *
   * @param http
   * @throws Exception
   */
  private void enableAuth(HttpSecurity http) throws Exception {
    JwtWebSecurityConfigurer.forRS256(apiAudience, issuer)
        .configure(http)
        .authorizeRequests()
        .antMatchers(V1_MENU_ENDPOINT)
        .authenticated()
        .antMatchers(V2_MENU_ENDPOINT)
        .authenticated()
        .antMatchers(V1_MENU)
        .authenticated()
        .antMatchers(V2_MENU)
        .authenticated();
  }

  /**
   * permitAll() - endpoints are not protected
   *
   * @param http
   * @throws Exception
   */
  private void permitAll(HttpSecurity http) throws Exception {
    JwtWebSecurityConfigurer.forRS256(apiAudience, issuer)
        .configure(http)
        .authorizeRequests()
        .antMatchers("/**")
        .permitAll();
  }
}
