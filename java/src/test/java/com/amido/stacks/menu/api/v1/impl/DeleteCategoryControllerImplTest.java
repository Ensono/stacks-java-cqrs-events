package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.domain.CategoryHelper.createCategories;
import static com.amido.stacks.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.menu.domain.ItemHelper.createItem;
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

import com.amido.stacks.Application;
import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.repository.MenuRepository;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@EnableAutoConfiguration(
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class DeleteCategoryControllerImplTest {

  public static final String DELETE_CATEGORY = "%s/v1/menu/%s/category/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository repository;

  @Test
  void testDeleteCategorySuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    menu.setCategories(List.of(category));
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(repository, times(1)).save(menu);
    then(response.getStatusCode()).isEqualTo(OK);
    Optional<Menu> optMenu = repository.findById(menu.getId());
    Menu updated = optMenu.get();
    then(updated.getCategories()).isNotNull();
  }

  @Test
  void testDeleteCategoryWithAnItem() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    category.addOrUpdateItem(createItem(0));
    menu.addOrUpdateCategory(category);

    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(repository, times(1)).save(captor.capture());
    Menu updatedMenu = captor.getValue();
    then(updatedMenu.getCategories()).isNotNull();
  }

  @Test
  void testDeleteCategoryWithInvalidCategoryId() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    menu.setCategories(List.of(category));
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), menu.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(repository, times(0)).save(menu);
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testDeleteACategoryFromList() {
    // Given
    Menu menu = createMenu(1);
    List<Category> categories = createCategories(2);
    menu.setCategories(categories);
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), categories.get(0).getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(repository, times(1)).save(menu);
    then(response.getStatusCode()).isEqualTo(OK);
    Optional<Menu> byId = repository.findById(menu.getId());
    Menu updatedMenu = byId.get();
    then(updatedMenu.getCategories()).hasSize(1);
  }
}
