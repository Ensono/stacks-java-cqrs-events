package com.amido.stacks.workloads.menu.api.v2;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.amido.stacks.workloads.Application;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.domain.utility.MenuHelper;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
#if DYNAMODB
import com.amido.stacks.workloads.menu.repository.MenuRepositoryDynamoDb;

#elif COSMOSDB
import com.amido.stacks.workloads.menu.repository.CosmosMenuRepository;
#endif
import com.amido.stacks.workloads.util.TestHelper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@TestPropertySource(
    properties = {
      "management.port=0",
      "aws.xray.enabled=false",
      "aws.secretsmanager.enabled=false",
      "spring.autoconfigure.exclude=com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration,"
          + "com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration,"
          + "com.azure.spring.autoconfigure.cosmos.CosmosHealthConfiguration"
    })
@Tag("Integration")
@ActiveProfiles("test")
class MenuControllerV2Test {

  private final String GET_MENU_BY_ID = "%s/v2/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  #if DYNAMODB
  @MockBean private MenuRepositoryDynamoDb menuRepository;

  #elif COSMOSDB
  @MockBean private CosmosMenuRepository menuRepository;
  #endif
  @Autowired private MenuMapper menuMapper;

  @Test
  void getMenuById() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    MenuDTO expectedResponse = menuMapper.toDto(menu);

    when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, TestHelper.getBaseURL(port), menu.getId()),
            MenuDTO.class);

    // Then
    then(response.getBody()).isEqualTo(expectedResponse);
  }

  @Test
  void getMenuByInvalidId() {
    // Given
    Menu menu = MenuHelper.createMenu(0);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, TestHelper.getBaseURL(port), UUID.randomUUID()),
            MenuDTO.class);

    // Then
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }
}
