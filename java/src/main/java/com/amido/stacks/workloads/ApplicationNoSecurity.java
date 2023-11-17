package com.amido.stacks.workloads;


import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Exclude security for test profile Add @ActiveProfiles("test") to exclude test from security (for
 * Ex: Auth0)
 */
@Configuration
@EnableWebSecurity
@Profile("test")
public class ApplicationNoSecurity {

  /**
   * allows configuration of web-based security at a resource level, based on a selection match -
   * e.g. The example below restricts the URLs that start with /admin/ to users that have ADMIN
   * role, and declares that any other URLs need to be successfully authenticated.
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers("/**");
  }

  /**
   * is used for configuration settings that impact global security (ignore resources, set debug
   * mode, reject requests by implementing a custom firewall definition). For example, the following
   * method would cause any request that starts with /resources/ to be ignored for authentication
   * purposes.
   *
   * @param http
   * @throws Exception
   */
  @Bean(name = "test_SecurityFilterChain")
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth.requestMatchers("**/*").anonymous())
        .httpBasic(withDefaults());
    return http.build();
  }
}
