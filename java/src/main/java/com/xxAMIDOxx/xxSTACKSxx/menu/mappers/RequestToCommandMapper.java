package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateMenuCommand;
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
