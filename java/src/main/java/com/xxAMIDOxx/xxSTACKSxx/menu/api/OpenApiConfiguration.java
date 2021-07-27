package com.xxAMIDOxx.xxSTACKSxx.menu.api;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
public class OpenApiConfiguration {

  private static final String PROJECT_URL = "https://github.com/amido/stacks-java";
  private static final String STACKS_EMAIL = "stacks@amido.com";
  private static final String SPRING_DOC = "http://springdoc.org";

  /** OAS/Swagger Configuration. */
  @Bean
  public OpenAPI customOpenApi() {
    List<Server> servers = new ArrayList<>();
    servers.add(new Server().url("/"));
    servers.add(new Server().url("/api/menu"));
    return new OpenAPI()
        .servers(servers)
        .info(
            new Info()
                .title("Menu API")
                .version("1.0")
                .description("APIs used to interact and manage menus for a restaurant")
                .contact(new Contact().name("Amido").url(PROJECT_URL).email(STACKS_EMAIL))
                .license(new License().name("Apache 2.0").url(SPRING_DOC)));
  }

  /**
   * Display all the api versions.
   *
   * @return list of all the api versions.
   */
  @Bean
  public GroupedOpenApi menuApiAll() {
    return GroupedOpenApi.builder().group("Menu (all)").pathsToMatch("/**").build();
  }

  /**
   * Display Version 1 the api versions.
   *
   * @return list of all the V1 api versions.
   */
  @Bean
  public GroupedOpenApi menuApiV1() {
    return GroupedOpenApi.builder().group("Menu (version 1)").pathsToMatch("/v1/**").build();
  }

  /**
   * Display all the V2 api versions.
   *
   * @return list of all the V2 api versions.
   */
  @Bean
  public GroupedOpenApi menuApiV2() {
    return GroupedOpenApi.builder().group("Menu (version 2)").pathsToMatch("/v2/**").build();
  }
}
