package com.amido.stacks.workloads.menu.api.v1.impl;

import static com.azure.cosmos.implementation.Utils.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.Application;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.domain.MenuHelper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.util.TestHelper;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosHealthConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@EnableAutoConfiguration(
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class,
        CosmosHealthConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class CreateCategoryControllerImplTest {

  public static final String CREATE_CATEGORY = "%s/v1/menu/%s/category";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @MockBean
  private MenuRepository menuRepository;

  @Test
  void testCanNotAddCategoryIfMenuNotPresent() {
    // Given
    UUID menuId = randomUUID();
    when(menuRepository.findById(eq(menuId.toString()))).thenReturn(Optional.empty());

    CreateCategoryRequest request =
        new CreateCategoryRequest("test Category Name", "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, TestHelper.getBaseURL(port), menuId),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testNoDescriptionGivenReturnsBadRequest() {
    // Given
    CreateCategoryRequest request = new CreateCategoryRequest("test Category Name", "");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, TestHelper.getBaseURL(port), randomUUID()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testInvalidMenuIdWilThrowBadRequest() {
    // Given
    CreateCategoryRequest request = new CreateCategoryRequest("test Category Name", "");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, TestHelper.getBaseURL(port), "XXXXXX"),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  void testAddCategory() {
    // Given
    Menu menu = MenuHelper.createMenu(1);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

    CreateCategoryRequest request =
        new CreateCategoryRequest("test Category Name", "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, TestHelper.getBaseURL(port), menu.getId()),
            request,
            ResourceCreatedResponse.class);

    // Then
    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu created = captor.getValue();

    then(created.getName()).isEqualTo(menu.getName());
    then(created.getDescription()).isEqualTo(menu.getDescription());
    then(created.getCategories().size()).isEqualTo(1);
    then(created.getCategories().get(0).getName()).isEqualTo(request.getName());
    then(created.getCategories().get(0).getDescription()).isEqualTo(request.getDescription());

    then(response).isNotNull();
    then(response.getBody()).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    then(created.getCategories().get(0).getId()).isEqualTo(response.getBody().getId().toString());
  }

  @Test
  void testCannotAddCategoryWhichAlreadyExists() {
    // Given
    Menu menu = MenuHelper.createMenu(1);
    Category category =
        new Category(
            UUID.randomUUID().toString(), "cat name", "cat description", new ArrayList<>());
    menu.addOrUpdateCategory(category);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    CreateCategoryRequest request =
        new CreateCategoryRequest(category.getName(), "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, TestHelper.getBaseURL(port), menu.getId()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void testNoNameGivenReturnsBadRequest() {
    // Given
    CreateCategoryRequest request = new CreateCategoryRequest("", "Some description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, TestHelper.getBaseURL(port), randomUUID()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }
}
