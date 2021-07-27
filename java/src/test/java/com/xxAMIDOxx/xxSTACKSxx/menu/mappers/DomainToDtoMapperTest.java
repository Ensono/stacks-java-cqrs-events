package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.CategoryDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ItemDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class DomainToDtoMapperTest {

  @Test
  void menuToMenuDto() {

    // Given
    UUID id = randomUUID();
    UUID restaurantId = randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Boolean enabled = true;

    Menu menu =
        new Menu(
            id.toString(),
            restaurantId.toString(),
            name,
            description,
            Collections.emptyList(),
            enabled);

    // When
    MenuDTO menuDTO = DomainToDtoMapper.toMenuDto(menu);

    // Then
    assertThat(menuDTO.getId()).isEqualTo(id);
    assertThat(menuDTO.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(menuDTO.getName()).isEqualTo(name);
    assertThat(menuDTO.getDescription()).isEqualTo(description);
    assertThat(menuDTO.getCategories()).isEqualTo(Collections.emptyList());
    assertThat(menuDTO.getEnabled()).isEqualTo(enabled);
  }

  @Test
  void menuToMenuDtoWithNullCategories() {

    // Given
    UUID id = randomUUID();
    UUID restaurantId = randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Boolean enabled = true;

    Menu menu = new Menu(id.toString(), restaurantId.toString(), name, description, null, enabled);

    // When
    MenuDTO menuDTO = DomainToDtoMapper.toMenuDto(menu);

    // Then
    assertThat(menuDTO.getId()).isEqualTo(id);
    assertThat(menuDTO.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(menuDTO.getName()).isEqualTo(name);
    assertThat(menuDTO.getDescription()).isEqualTo(description);
    assertThat(menuDTO.getCategories()).isNull();
    assertThat(menuDTO.getEnabled()).isEqualTo(enabled);
  }

  @Test
  void menuToMenuDtoWithNullCategoryItems() {

    // Given
    UUID id = randomUUID();
    UUID restaurantId = randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Boolean enabled = true;

    UUID categoryId = randomUUID();
    Category category = new Category(categoryId.toString(), "aaaaaa", "bbbbbb", null);

    Menu menu =
        new Menu(
            id.toString(),
            restaurantId.toString(),
            name,
            description,
            Arrays.asList(category),
            enabled);

    // When
    MenuDTO menuDTO = DomainToDtoMapper.toMenuDto(menu);

    // Then
    assertThat(menuDTO.getId()).isEqualTo(id);
    assertThat(menuDTO.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(menuDTO.getName()).isEqualTo(name);
    assertThat(menuDTO.getDescription()).isEqualTo(description);
    assertThat(menuDTO.getEnabled()).isEqualTo(enabled);
    assertThat(menuDTO.getCategories().size()).isEqualTo(1);
    assertThat(menuDTO.getCategories().get(0).getName()).isEqualTo(category.getName());
    assertThat(menuDTO.getCategories().get(0).getDescription())
        .isEqualTo(category.getDescription());
    assertThat(menuDTO.getCategories().get(0).getId()).isEqualTo(category.getId());
    assertThat(menuDTO.getCategories().get(0).getItems()).isNull();
  }

  @Test
  void menuToSearchMenuResultItem() {

    // Given
    UUID id = randomUUID();
    UUID restaurantId = randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Boolean enabled = true;

    Menu menu = new Menu(id.toString(), restaurantId.toString(), name, description, null, enabled);

    // When
    SearchMenuResultItem resultItem = DomainToDtoMapper.toSearchMenuResultItem(menu);

    // Then
    assertThat(resultItem.getId()).isEqualTo(id);
    assertThat(resultItem.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(resultItem.getName()).isEqualTo(name);
    assertThat(resultItem.getDescription()).isEqualTo(description);
    assertThat(resultItem.getEnabled()).isEqualTo(enabled);
  }

  @Test
  void itemToItemDto() {
    // Given
    String id = randomUUID().toString();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Double price = 15.1d;
    Boolean available = true;

    Item item = new Item(id, name, description, price, available);

    // When
    ItemDTO itemDTO = DomainToDtoMapper.toItemDto(item);

    // Then
    assertThat(itemDTO.getId()).isEqualTo(id);
    assertThat(itemDTO.getName()).isEqualTo(name);
    assertThat(itemDTO.getDescription()).isEqualTo(description);
    assertThat(itemDTO.getPrice()).isEqualTo(price);
    assertThat(itemDTO.getAvailable()).isEqualTo(available);
  }

  @Test
  void categoryToCategoryDto() {
    // Given
    String id = randomUUID().toString();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";

    Category category = new Category(id, name, description, Collections.emptyList());

    // When
    CategoryDTO categoryDTO = DomainToDtoMapper.toCategoryDto(category);

    // Then
    assertThat(categoryDTO.getId()).isEqualTo(id);
    assertThat(categoryDTO.getName()).isEqualTo(name);
    assertThat(categoryDTO.getDescription()).isEqualTo(description);
    assertThat(categoryDTO.getItems()).isEqualTo(Collections.emptyList());
  }
}
