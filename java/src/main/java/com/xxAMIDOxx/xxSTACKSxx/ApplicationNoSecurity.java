package com.xxAMIDOxx.xxSTACKSxx;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Exclude security for test profile Add @ActiveProfiles("test") to exclude test from security (for
 * Ex: Auth0)
 */
@Configuration
@EnableWebSecurity
@Profile("test")
public class ApplicationNoSecurity extends WebSecurityConfigurerAdapter {

  /**
   * allows configuration of web-based security at a resource level, based on a selection match -
   * e.g. The example below restricts the URLs that start with /admin/ to users that have ADMIN
   * role, and declares that any other URLs need to be successfully authenticated.
   *
   * @param web
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/**");
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
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.antMatcher("**/*").anonymous();
  }
}
