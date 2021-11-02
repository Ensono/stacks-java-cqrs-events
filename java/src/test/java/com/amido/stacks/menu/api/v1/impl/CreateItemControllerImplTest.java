package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.amido.stacks.Application;
import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.repository.MenuRepository;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.ArrayList;
import java.util.Arrays;
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
    exclude = {CosmosRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class CreateItemControllerImplTest {

  public static final String CREATE_ITEM = "%s/v1/menu/%s/category/%s/items";
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository menuRepository;

  @Test
  void testAddItem() {
    // Given
    Menu menu = createMenu(1);
    Category category =
        new Category(randomUUID().toString(), "cat name", "cat description", new ArrayList<>());
    menu.addOrUpdateCategory(category);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

    CreateItemRequest request =
        new CreateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), category.getId()),
            request,
            ResourceCreatedResponse.class);

    // Then
    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu created = captor.getValue();

    then(created.getName()).isEqualTo(menu.getName());
    then(created.getDescription()).isEqualTo(menu.getDescription());
    then(created.getCategories().size()).isEqualTo(1);
    then(created.getCategories().get(0).getName()).isEqualTo(category.getName());
    then(created.getCategories().get(0).getDescription()).isEqualTo(category.getDescription());

    then(created.getCategories().get(0).getItems()).isNotNull();
    then(created.getCategories().get(0).getItems().size()).isEqualTo(1);

    Item createdItem = created.getCategories().get(0).getItems().get(0);
    then(createdItem.getName()).isEqualTo(request.getName());
    then(createdItem.getDescription()).isEqualTo(request.getDescription());
    then(createdItem.getPrice()).isEqualTo(request.getPrice());
    then(createdItem.getAvailable()).isEqualTo(request.getAvailable());

    then(response).isNotNull();
    then(response.getBody()).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    then(createdItem.getId()).isEqualTo(response.getBody().getId().toString());
  }

  @Test
  void testInvalidCategoryIdWilThrowBadRequest() {

    // Given
    CreateItemRequest request =
        new CreateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), randomUUID(), "xyz"),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  void testAddItemWhenInvalidCategoryIdGiven() {

    // Given
    Menu menu = createMenu(1);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    CreateItemRequest request =
        new CreateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), randomUUID()),
            request,
            ErrorResponse.class);

    // Then
    verify(menuRepository, never()).save(any(Menu.class));

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testCannotAddItemWhichAlreadyExists() {
    // Given
    Menu menu = createMenu(1);
    Item item = new Item(randomUUID().toString(), "item name", "item description", 5.99d, true);
    Category category =
        new Category(
            UUID.randomUUID().toString(), "cat name", "cat description", Arrays.asList(item));
    menu.addOrUpdateCategory(category);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    CreateItemRequest request =
        new CreateItemRequest(item.getName(), "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), category.getId()),
            request,
            ErrorResponse.class);

    // Then
    verify(menuRepository, never()).save(any(Menu.class));

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(CONFLICT);
  }

  @Test
  void testNoItemNameReturnsBadRequest() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(1);
    menu.addOrUpdateCategory(category);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);
    CreateItemRequest request = new CreateItemRequest("", "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), category.getId()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }

  @Test
  void testNoItemDescriptionReturnsBadRequest() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(1);
    menu.addOrUpdateCategory(category);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);
    CreateItemRequest request = new CreateItemRequest("Some name", "", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), category.getId()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testInvalidPriceDescriptionReturnsBadRequest() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(1);
    menu.addOrUpdateCategory(category);

    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);
    CreateItemRequest request = new CreateItemRequest("Some name", "Item description", 0d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), category.getId()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {price=Price must be greater than zero}");
  }
}
