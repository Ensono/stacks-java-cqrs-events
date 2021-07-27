package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryCommand extends MenuCommand {

  private String name;
  private String description;
  private UUID categoryId;

  public CreateCategoryCommand(String correlationId, UUID menuId, String name, String description) {
    super(OperationCode.CREATE_CATEGORY, correlationId, menuId);
    this.name = name;
    this.description = description;
  }
}
