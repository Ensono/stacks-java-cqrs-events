package com.amido.stacks.menu.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCategoryCommand extends MenuCommand {

  private static final int OPERATION_CODE = 201;

  private String name;
  private String description;
  private UUID categoryId;

  public CreateCategoryCommand(String correlationId, UUID menuId, String name, String description) {
    super(correlationId, menuId);
    this.name = name;
    this.description = description;
  }

  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
