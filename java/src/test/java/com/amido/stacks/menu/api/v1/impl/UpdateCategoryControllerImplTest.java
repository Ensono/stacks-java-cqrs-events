package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.domain.CategoryHelper.createCategories;
import static com.amido.stacks.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static com.amido.stacks.util.TestHelper.getRequestHttpEntity;
import static com.azure.cosmos.implementation.Utils.randomUUID;
import static java.util.UUID.fromString;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.Application;
import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.repository.MenuRepository;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.List;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@EnableAutoConfiguration(
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class UpdateCategoryControllerImplTest {

  public static final String UPDATE_CATEGORY = "%s/v1/menu/%s/category/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository menuRepository;

  @Test
  void testUpdateCategorySuccess() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY,
            getBaseURL(port),
            fromString(menu.getId()),
            fromString(category.getId()));

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu updated = captor.getValue();
    then(updated.getCategories()).hasSize(1);
    Category updatedCategory = updated.getCategories().get(0);
    then(updatedCategory.getDescription()).isEqualTo(request.getDescription());
    then(updatedCategory.getName()).isEqualTo(request.getName());
  }

  @Test
  void testCannotUpdateCategoryIfNoMenuExists() {
    // Given
    UUID menuId = randomUUID();
    when(menuRepository.findById(eq(menuId.toString()))).thenReturn(Optional.empty());

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl = String.format(UPDATE_CATEGORY, getBaseURL(port), menuId, randomUUID());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testCannotUpdateCategoryIfNotAlreadyExists() {
    // Given
    Menu menu = createMenu(0);
    menu.setCategories(null);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY, getBaseURL(port), fromString(menu.getId()), UUID.randomUUID());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testCannotUpdateCategoryNameIfNameAlreadyExists() {
    // Given
    Menu menu = createMenu(0);
    List<Category> categoryList = createCategories(2);
    categoryList.get(0).setName("new Category");

    menu.setCategories(categoryList);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY,
            getBaseURL(port),
            fromString(menu.getId()),
            fromString(categoryList.get(1).getId()));
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(CONFLICT);
  }

  @Test
  void testUpdateCategoryWhenMultipleCategoriesExists() {
    // Given
    Menu menu = createMenu(0);
    List<Category> categories = createCategories(2);
    menu.setCategories(categories);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl =
        String.format(UPDATE_CATEGORY, getBaseURL(port), menu.getId(), categories.get(0).getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu updated = captor.getValue();
    then(updated.getCategories()).hasSize(2);
    Optional<Category> updatedCategory =
        menu.getCategories().stream()
            .filter(c -> c.getId().equals(categories.get(0).getId()))
            .findFirst();
    then(updatedCategory.get().getDescription()).isEqualTo(request.getDescription());
    then(updatedCategory.get().getName()).isEqualTo(request.getName());
  }

  @Test
  void testUpdateOnlyCategoryDescription() {
    // Given
    Menu menu = createMenu(0);
    List<Category> categoryList = createCategories(2);
    menu.setCategories(categoryList);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request =
        new UpdateCategoryRequest(categoryList.get(1).getName(), "new Description");

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY,
            getBaseURL(port),
            fromString(menu.getId()),
            fromString(categoryList.get(1).getId()));
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu updated = captor.getValue();
    then(updated.getCategories()).hasSize(2);

    Category updatedCategory = updated.getCategories().get(1);
    then(updatedCategory.getDescription()).isEqualTo(request.getDescription());
    then(updatedCategory.getName()).isEqualTo(request.getName());

    Category originalCategory = updated.getCategories().get(0);
    then(originalCategory.getDescription()).isEqualTo(categoryList.get(0).getDescription());
    then(originalCategory.getName()).isEqualTo(categoryList.get(0).getName());
  }

  @Test
  void testUpdateCategoryWithNoNameReturnsBadRequest() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request = new UpdateCategoryRequest("", "new Description");

    // When
    String requestUrl =
        String.format(UPDATE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }

  @Test
  void testUpdateCategoryWithNoDescriptionReturnsBadRequest() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request = new UpdateCategoryRequest("Updated Name", "");

    // When
    String requestUrl =
        String.format(UPDATE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }
}
