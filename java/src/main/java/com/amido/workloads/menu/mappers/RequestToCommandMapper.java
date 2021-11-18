package com.amido.workloads.menu.mappers;

import com.amido.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.workloads.menu.commands.CreateCategoryCommand;
import com.amido.workloads.menu.commands.CreateItemCommand;
import com.amido.workloads.menu.commands.CreateMenuCommand;
import com.amido.workloads.menu.commands.UpdateCategoryCommand;
import com.amido.workloads.menu.commands.UpdateItemCommand;
import com.amido.workloads.menu.commands.UpdateMenuCommand;
import java.util.UUID;

public class RequestToCommandMapper {

  public static CreateMenuCommand map(String correlationId, CreateMenuRequest r) {
    return new CreateMenuCommand(
        correlationId, r.getName(), r.getDescription(), r.getTenantId(), r.getEnabled());
  }

  public static UpdateMenuCommand map(String correlationId, UUID menuId, UpdateMenuRequest r) {
    return new UpdateMenuCommand(
        correlationId, menuId, r.getName(), r.getDescription(), r.getEnabled());
  }

  public static CreateCategoryCommand map(
      String correlationId, UUID menuId, CreateCategoryRequest r) {
    return new CreateCategoryCommand(correlationId, menuId, r.getName(), r.getDescription());
  }

  public static UpdateCategoryCommand map(
      String correlationId, UUID menuId, UUID categoryId, UpdateCategoryRequest r) {
    return new UpdateCategoryCommand(
        correlationId, menuId, categoryId, r.getName(), r.getDescription());
  }

  public static CreateItemCommand map(
      String correlationId, UUID menuId, UUID categoryId, CreateItemRequest r) {
    return new CreateItemCommand(
        correlationId,
        menuId,
        categoryId,
        r.getName(),
        r.getDescription(),
        r.getPrice(),
        r.getAvailable());
  }

  public static UpdateItemCommand map(
      String correlationId, UUID menuId, UUID categoryId, UUID itemId, UpdateItemRequest r) {
    return new UpdateItemCommand(
        correlationId,
        menuId,
        categoryId,
        itemId,
        r.getName(),
        r.getDescription(),
        r.getPrice(),
        r.getAvailable());
  }
}
