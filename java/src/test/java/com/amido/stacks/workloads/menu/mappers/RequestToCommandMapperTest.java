package com.amido.stacks.workloads.menu.mappers;

import static org.junit.Assert.assertEquals;

import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.commands.CreateCategoryCommand;
import com.amido.stacks.workloads.menu.commands.CreateItemCommand;
import com.amido.stacks.workloads.menu.commands.CreateMenuCommand;
import com.amido.stacks.workloads.menu.mappers.commands.CreateCategoryMapper;
import com.amido.stacks.workloads.menu.mappers.commands.CreateCategoryMapperImpl;
import com.amido.stacks.workloads.menu.mappers.commands.CreateItemMapper;
import com.amido.stacks.workloads.menu.mappers.commands.CreateItemMapperImpl;
import com.amido.stacks.workloads.menu.mappers.commands.CreateMenuMapper;
import com.amido.stacks.workloads.menu.mappers.commands.CreateMenuMapperImpl;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateCategoryMapper;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateCategoryMapperImpl;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateItemMapper;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateItemMapperImpl;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateMenuMapper;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateMenuMapperImpl;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("Unit")
@SpringBootTest(
    classes = {
      CreateMenuMapper.class,
      CreateMenuMapperImpl.class,
      UpdateMenuMapper.class,
      UpdateMenuMapperImpl.class,
      CreateCategoryMapper.class,
      CreateCategoryMapperImpl.class,
      UpdateCategoryMapper.class,
      UpdateCategoryMapperImpl.class,
      CreateItemMapper.class,
      CreateItemMapperImpl.class,
      UpdateItemMapper.class,
      UpdateItemMapperImpl.class,
      RequestToCommandMapper.class
    })
class RequestToCommandMapperTest {

  @Autowired private RequestToCommandMapper requestToCommandMapper;

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
    CreateMenuCommand command = requestToCommandMapper.map(correlationId, request);

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
    CreateCategoryCommand command = requestToCommandMapper.map(correlationId, menuId, request);

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
        requestToCommandMapper.map(correlationId, menuId, categoryId, request);

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
