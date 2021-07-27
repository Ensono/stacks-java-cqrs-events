package com.amido.stacks.menu.api.v2.impl;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import com.amido.stacks.menu.repository.MenuRepository;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class QueryMenuControllerImplV2Test {

  private final String GET_MENU_BY_ID = "%s/v2/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository menuRepository;

  @Test
  void getMenuById() {
    // Given
    Menu menu = createMenu(0);
    MenuDTO expectedResponse = DomainToDtoMapper.toMenuDto(menu);

    when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, getBaseURL(port), menu.getId()), MenuDTO.class);

    // Then
    then(response.getBody()).isEqualTo(expectedResponse);
  }

  @Test
  void getMenuByInvalidId() {
    // Given
    Menu menu = createMenu(0);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, getBaseURL(port), UUID.randomUUID()), MenuDTO.class);

    // Then
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }
}
