package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class RequestToCommandMapperTest {

  @Test
  void createMenuRequestToCommand() {

    // Given
    String correlationId = "ccc";
    String name = "xxx";
    String description = "yyy";
    UUID tenantId = UUID.randomUUID();
    Boolean enabled = true;
    CreateMenuRequest request = new CreateMenuRequest(name, description, tenantId, enabled);

    // When
    CreateMenuCommand command = RequestToCommandMapper.map(correlationId, request);

    // Then
    assertEquals(correlationId, command.getCorrelationId());
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
    assertEquals(tenantId, command.getRestaurantId());
    assertEquals(enabled, command.getEnabled());
  }

  @Test
  void createCategoryRequestToCommand() {

    // Given
    String correlationId = "ccc";
    UUID menuId = UUID.randomUUID();
    String name = "xxx";
    String description = "yyy";
    CreateCategoryRequest request = new CreateCategoryRequest(name, description);

    // When
    CreateCategoryCommand command = RequestToCommandMapper.map(correlationId, menuId, request);

    // Then
    assertEquals(correlationId, command.getCorrelationId());
    assertEquals(menuId, command.getMenuId());
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
  }

  @Test
  void createItemRequestToCommand() {

    // Given
    String correlationId = "ccc";
    UUID menuId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    String name = "xxx";
    String description = "yyy";
    Double price = 2.50;
    Boolean available = false;
    CreateItemRequest request = new CreateItemRequest(name, description, price, available);

    // When
    CreateItemCommand command =
        RequestToCommandMapper.map(correlationId, menuId, categoryId, request);

    // Then
    assertEquals(correlationId, command.getCorrelationId());
    assertEquals(menuId, command.getMenuId());
    assertEquals(categoryId, command.getCategoryId());
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
    assertEquals(price, command.getPrice());
    assertEquals(available, command.getAvailable());
  }
}
