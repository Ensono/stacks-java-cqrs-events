package com.amido.stacks.workloads.menu.api.v1;

import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenus;
import static com.amido.stacks.workloads.util.TestHelper.getBaseURL;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.Application;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResultItem;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.domain.MenuHelper;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.util.TestHelper;
import com.azure.cosmos.implementation.Utils;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosHealthConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@EnableAutoConfiguration(
    exclude = {
      CosmosRepositoriesAutoConfiguration.class,
      CosmosAutoConfiguration.class,
      CosmosHealthConfiguration.class
    })
@Tag("Integration")
@ActiveProfiles("test")
public class MenuControllerTest {

  public static final String CREATE_MENU = "/v1/menu";
  public static final String UPDATE_MENU = "%s/v1/menu/%s";

  public static final String DELETE_MENU = "%s/v1/menu/%s";

  final int DEFAULT_PAGE_NUMBER = 1;
  final int DEFAULT_PAGE_SIZE = 20;

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository menuRepository;

  @Autowired private MenuMapper menuMapper;

  @Autowired private SearchMenuResultItemMapper searchMenuResultItemMapper;

  @Test
  void testCreateNewMenu() {
    // Given
    Menu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            m.getName(), m.getDescription(), UUID.fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepository.findAllByRestaurantIdAndName(
            eq(m.getRestaurantId()), eq(m.getName()), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));
    when(menuRepository.save(any(Menu.class))).thenReturn(m);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ResourceCreatedResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void testThrowsErrorOnExists() {
    // Given
    Menu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            m.getName(), m.getDescription(), UUID.fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepository.findAllByRestaurantIdAndName(
            eq(m.getRestaurantId()), eq(m.getName()), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Arrays.asList(m)));

    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void testWhenDescriptionNotGivenReturnsBadRequest() {
    // Given
    Menu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(m.getName(), "", fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepository.save(any(Menu.class))).thenReturn(m);
    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testWhenNoNameReturnsBadRequest() {
    // Given
    Menu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            "", m.getDescription(), fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepository.save(any(Menu.class))).thenReturn(m);
    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }

  @Test
  public void listMenusAndPagination() {

    // Given
    when(menuRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(createMenus(1)));

    int pageNumber = 5;
    int pageSize = 6;

    // When
    var response =
        this.testRestTemplate.getForEntity(
            TestHelper.getBaseURL(port)
                + String.format("/v1/menu?pageSize=%d&pageNumber=%d", pageSize, pageNumber),
            SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();

    // Then
    verify(menuRepository, times(1)).findAll(any(Pageable.class));
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(pageNumber));
    assertThat(actual.getPageSize(), is(pageSize));
  }

  @Test
  public void listMenusFilteredByRestaurantId() {

    // Given
    UUID restaurantId = randomUUID();

    List<Menu> menuList = createMenus(3);
    Menu match = menuList.get(0);
    match.setRestaurantId(restaurantId.toString());
    menuList.add(match);
    List<Menu> matching = Collections.singletonList(match);

    List<SearchMenuResultItem> expectedMenuList =
        matching.stream()
            .map(m -> searchMenuResultItemMapper.toDto(m))
            .collect(Collectors.toList());

    SearchMenuResult expectedResponse =
        new SearchMenuResult(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER, expectedMenuList);

    when(menuRepository.findAllByRestaurantId(eq(restaurantId.toString()), any(Pageable.class)))
        .thenReturn(new PageImpl<>(matching));

    // When
    var result =
        this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu?restaurantId=%s", TestHelper.getBaseURL(port), restaurantId),
            SearchMenuResult.class);
    // Then
    then(result.getBody().getPageNumber()).isEqualTo(expectedResponse.getPageNumber());
    then(result.getBody().getPageSize()).isEqualTo(expectedResponse.getPageSize());
    then(result.getBody().getResults()).containsAll(expectedResponse.getResults());
  }

  @Test
  public void listMenusFilteredByRestaurantIdAndSearchTerm() {
    // Given
    UUID restaurantId = randomUUID();
    final String searchTerm = "searchTermString";

    when(menuRepository.findAllByRestaurantIdAndNameContaining(
            eq(restaurantId.toString()), eq(searchTerm), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));

    // When
    this.testRestTemplate.getForEntity(
        String.format(
            "%s/v1/menu?restaurantId=%s&searchTerm=%s",
            TestHelper.getBaseURL(port), restaurantId, searchTerm),
        SearchMenuResult.class);
    // Then
    verify(menuRepository, times(1))
        .findAllByRestaurantIdAndNameContaining(
            eq(restaurantId.toString()), eq(searchTerm), any(Pageable.class));
  }

  @Test
  public void listMenusFilteredBySearchTerm() {
    // Given
    final String searchTerm = "searchTermString";

    when(menuRepository.findAllByNameContaining(eq(searchTerm), any(Pageable.class)))
        .thenReturn(new PageImpl<>(createMenus(0)));

    // When
    this.testRestTemplate.getForEntity(
        String.format("%s/v1/menu?searchTerm=%s", TestHelper.getBaseURL(port), searchTerm),
        SearchMenuResult.class);
    // Then
    verify(menuRepository, times(1)).findAllByNameContaining(eq(searchTerm), any(Pageable.class));
  }

  @Test
  public void getMenuById() {
    // Given
    Menu menu = createMenu(0);
    Item item = new Item(randomUUID().toString(), "item name", "item description", 5.99d, true);
    Category category =
        new Category(
            UUID.randomUUID().toString(), "cat name", "cat description", Arrays.asList(item));
    menu.addOrUpdateCategory(category);

    MenuDTO expectedResponse = menuMapper.toDto(menu);

    when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu/%s", TestHelper.getBaseURL(port), menu.getId()),
            MenuDTO.class);

    // Then
    then(response.getBody()).isEqualTo(expectedResponse);
  }

  @Test
  public void listMenusWithDefaultPagination() {
    // Given
    when(menuRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(createMenus(1)));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            TestHelper.getBaseURL(port) + "/v1/menu", SearchMenuResult.class);

    // Then
    then(response.getBody()).isInstanceOf(SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(DEFAULT_PAGE_NUMBER));
    assertThat(actual.getPageSize(), is(DEFAULT_PAGE_SIZE));
  }

  @Test
  void testUpdateSuccess() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateMenuRequest request = new UpdateMenuRequest("new name", "new description", false);

    // When
    var response =
        this.testRestTemplate.exchange(
            String.format(UPDATE_MENU, TestHelper.getBaseURL(port), menu.getId()),
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu updated = captor.getValue();

    then(updated.getName()).isEqualTo(request.getName());
    then(updated.getDescription()).isEqualTo(request.getDescription());
    then(updated.getEnabled()).isEqualTo(request.getEnabled());
    then(updated.getRestaurantId()).isEqualTo(menu.getRestaurantId());

    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testCannotUpdateIfMenuDoesntExist() {
    // Given
    UUID menuId = Utils.randomUUID();
    when(menuRepository.findById(eq(menuId.toString()))).thenReturn(Optional.empty());

    UpdateMenuRequest request = new UpdateMenuRequest("name", "description", true);

    // When
    var response =
        this.testRestTemplate.exchange(
            String.format(UPDATE_MENU, TestHelper.getBaseURL(port), menuId),
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testUpdateMenuWithNoNameReturnsBadRequest() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateMenuRequest request = new UpdateMenuRequest("", "new description", false);

    // When
    var response =
        this.testRestTemplate.exchange(
            String.format(UPDATE_MENU, TestHelper.getBaseURL(port), menu.getId()),
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }

  @Test
  void testUpdateMenuWithNoDescriptionReturnsBadRequest() {
    // Given
    Menu menu = MenuHelper.createMenu(0);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateMenuRequest request = new UpdateMenuRequest("Updated Name", "", false);

    // When
    var response =
        this.testRestTemplate.exchange(
            String.format(UPDATE_MENU, TestHelper.getBaseURL(port), menu.getId()),
            HttpMethod.PUT,
            new HttpEntity<>(request, TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testDeleteMenuSuccess() {
    // Given
    Menu menu = MenuHelper.createMenu(1);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    var response =
        this.testRestTemplate.exchange(
            String.format(DELETE_MENU, TestHelper.getBaseURL(port), menu.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(TestHelper.getRequestHttpEntity()),
            ResponseEntity.class);
    // Then
    verify(menuRepository, times(1)).delete(menu);
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testDeleteMenuWithInvalidId() {
    // Given
    Menu menu = MenuHelper.createMenu(1);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    var response =
        this.testRestTemplate.exchange(
            String.format(DELETE_MENU, TestHelper.getBaseURL(port), UUID.randomUUID().toString()),
            HttpMethod.DELETE,
            new HttpEntity<>(TestHelper.getRequestHttpEntity()),
            ErrorResponse.class);
    // Then
    verify(menuRepository, times(0)).delete(menu);
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }
}
