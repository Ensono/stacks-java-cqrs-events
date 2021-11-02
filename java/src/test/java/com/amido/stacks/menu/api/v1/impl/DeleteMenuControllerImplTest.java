package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static com.amido.stacks.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.app.Application;
import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.repository.MenuRepository;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@EnableAutoConfiguration(
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class DeleteMenuControllerImplTest {

  public static final String DELETE_MENU = "%s/v1/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository repository;

  @Test
  void testDeleteMenuSuccess() {
    // Given
    Menu menu = createMenu(1);
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    var response =
        this.testRestTemplate.exchange(
            String.format(DELETE_MENU, getBaseURL(port), menu.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ResponseEntity.class);
    // Then
    verify(repository, times(1)).delete(menu);
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testDeleteMenuWithInvalidId() {
    // Given
    Menu menu = createMenu(1);
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    var response =
        this.testRestTemplate.exchange(
            String.format(DELETE_MENU, getBaseURL(port), UUID.randomUUID().toString()),
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);
    // Then
    verify(repository, times(0)).delete(menu);
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }
}
