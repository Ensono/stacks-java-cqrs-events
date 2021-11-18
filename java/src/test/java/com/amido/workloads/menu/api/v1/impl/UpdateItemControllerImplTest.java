package com.amido.workloads.menu.api.v1.impl;

import static com.amido.workloads.menu.domain.CategoryHelper.createCategory;
import static com.amido.workloads.menu.domain.ItemHelper.createItem;
import static com.amido.workloads.menu.domain.ItemHelper.createItems;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.workloads.Application;
import com.amido.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.amido.workloads.menu.domain.Category;
import com.amido.workloads.menu.domain.Item;
import com.amido.workloads.menu.domain.Menu;
import com.amido.workloads.menu.domain.MenuHelper;
import com.amido.workloads.menu.repository.MenuRepository;
import com.amido.workloads.util.TestHelper;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

/** @author ArathyKrishna */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@EnableAutoConfiguration(
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class UpdateItemControllerImplTest {

  public static final String UPDATE_ITEM = "%s/v1/menu/%s/category/%s/items/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository menuRepository;

  @Test
  void testUpdateItemSuccess() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    Item item = createItem(0);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request =
        new UpdateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, TestHelper.getBaseURL(port), menu.getId(), category.getId(), item.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu updated = captor.getValue();
    then(updated.getCategories()).hasSize(1);
    Category updatedCategory = updated.getCategories().get(0);
    then(updatedCategory.getItems()).hasSize(1);
    Item updatedItem = updatedCategory.getItems().get(0);

    then(updatedItem.getDescription()).isEqualTo(request.getDescription());
    then(updatedItem.getName()).isEqualTo(request.getName());
    then(updatedItem.getPrice()).isEqualTo(request.getPrice());
    then(updatedItem.getAvailable()).isTrue();
  }

  @Test
  void testUpdateItemDescription() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    List<Item> items = createItems(2);
    category.setItems(items);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request =
        new UpdateItemRequest(items.get(0).getName(), "Some Description2", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM,
            TestHelper.getBaseURL(port),
            menu.getId(),
            category.getId(),
            items.get(0).getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Category updatedCategory = captor.getValue().getCategories().get(0);
    then(updatedCategory.getItems()).hasSize(2);

    Item updatedItem = updatedCategory.getItems().get(1);
    then(updatedItem.getDescription()).isEqualTo(request.getDescription());
    then(updatedItem.getName()).isEqualTo(request.getName());
    then(updatedItem.getPrice()).isEqualTo(request.getPrice());
    then(updatedItem.getAvailable()).isTrue();

    Item nonUpdatedItem = updatedCategory.getItems().get(0);
    then(nonUpdatedItem.getDescription()).isEqualTo(items.get(1).getDescription());
    then(nonUpdatedItem.getName()).isEqualTo(items.get(1).getName());
    then(nonUpdatedItem.getPrice()).isEqualTo(items.get(1).getPrice());
    then(nonUpdatedItem.getAvailable()).isEqualTo(items.get(1).getAvailable());
  }

  @Test
  void testUpdateItemWithInvalidCategoryId() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    Item item = new Item(randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request =
        new UpdateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, TestHelper.getBaseURL(port), menu.getId(), randomUUID(), item.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(0)).save(captor.capture());
  }

  @Test
  void testUpdateItemWithInvalidItemId() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    Item item = new Item(randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request =
        new UpdateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, TestHelper.getBaseURL(port), menu.getId(), category.getId(), randomUUID());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(0)).save(captor.capture());
  }

  @Test
  void testUpdateItemWithNoNameReturnsBadRequest() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    Item item = new Item(randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request = new UpdateItemRequest("", "Some Description", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, TestHelper.getBaseURL(port), menu.getId(), category.getId(), randomUUID());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }

  @Test
  void testUpdateItemWithNoDescriptionReturnsBadRequest() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    Item item = createItem(0);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request = new UpdateItemRequest("Updated Name", "", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, TestHelper.getBaseURL(port), menu.getId(), category.getId(), randomUUID());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testUpdateItemWithInvalidPriceReturnsBadRequest() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    Category category = createCategory(0);
    Item item = createItem(0);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateItemRequest request = new UpdateItemRequest("Updated Name", "la alal", 0d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, TestHelper.getBaseURL(port), menu.getId(), category.getId(), randomUUID());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {price=Price must be greater than zero}");
  }
}
