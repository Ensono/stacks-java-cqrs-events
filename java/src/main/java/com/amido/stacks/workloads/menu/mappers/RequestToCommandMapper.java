package com.amido.stacks.workloads.menu.mappers;

import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.commands.CreateCategoryCommand;
import com.amido.stacks.workloads.menu.commands.CreateItemCommand;
import com.amido.stacks.workloads.menu.commands.CreateMenuCommand;
import com.amido.stacks.workloads.menu.commands.UpdateCategoryCommand;
import com.amido.stacks.workloads.menu.commands.UpdateItemCommand;
import com.amido.stacks.workloads.menu.commands.UpdateMenuCommand;
import com.amido.stacks.workloads.menu.mappers.commands.CreateCategoryMapper;
import com.amido.stacks.workloads.menu.mappers.commands.CreateItemMapper;
import com.amido.stacks.workloads.menu.mappers.commands.CreateMenuMapper;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateCategoryMapper;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateItemMapper;
import com.amido.stacks.workloads.menu.mappers.commands.UpdateMenuMapper;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestToCommandMapper {

  @Autowired private CreateMenuMapper createMenuMapper;

  @Autowired private UpdateMenuMapper updateMenuMapper;

  @Autowired private CreateCategoryMapper createCategoryMapper;

  @Autowired private UpdateCategoryMapper updateCategoryMapper;

  @Autowired private CreateItemMapper createItemMapper;

  @Autowired private UpdateItemMapper updateItemMapper;

  public CreateMenuCommand map(String correlationId, CreateMenuRequest r) {

    CreateMenuCommand createMenuCommand = createMenuMapper.fromDto(r);
    createMenuCommand.setCorrelationId(correlationId);

    return createMenuCommand;
  }

  public UpdateMenuCommand map(String correlationId, UUID menuId, UpdateMenuRequest r) {

    UpdateMenuCommand updateMenuCommand = updateMenuMapper.fromDto(r);
    updateMenuCommand.setCorrelationId(correlationId);
    updateMenuCommand.setMenuId(menuId);

    return updateMenuCommand;
  }

  public CreateCategoryCommand map(String correlationId, UUID menuId, CreateCategoryRequest r) {

    CreateCategoryCommand createCategoryCommand = createCategoryMapper.fromDto(r);
    createCategoryCommand.setCorrelationId(correlationId);
    createCategoryCommand.setMenuId(menuId);

    return createCategoryCommand;
  }

  public UpdateCategoryCommand map(
      String correlationId, UUID menuId, UUID categoryId, UpdateCategoryRequest r) {

    UpdateCategoryCommand updateCategoryCommand = updateCategoryMapper.fromDto(r);
    updateCategoryCommand.setCorrelationId(correlationId);
    updateCategoryCommand.setMenuId(menuId);
    updateCategoryCommand.setCategoryId(categoryId);

    return updateCategoryCommand;
  }

  public CreateItemCommand map(
      String correlationId, UUID menuId, UUID categoryId, CreateItemRequest r) {

    CreateItemCommand createItemCommand = createItemMapper.fromDto(r);
    createItemCommand.setCorrelationId(correlationId);
    createItemCommand.setMenuId(menuId);
    createItemCommand.setCategoryId(categoryId);

    return createItemCommand;
  }

  public UpdateItemCommand map(
      String correlationId, UUID menuId, UUID categoryId, UUID itemId, UpdateItemRequest r) {

    UpdateItemCommand updateItemCommand = updateItemMapper.fromDto(r);
    updateItemCommand.setCorrelationId(correlationId);
    updateItemCommand.setMenuId(menuId);
    updateItemCommand.setCategoryId(categoryId);
    updateItemCommand.setItemId(itemId);

    return updateItemCommand;
  }
}
