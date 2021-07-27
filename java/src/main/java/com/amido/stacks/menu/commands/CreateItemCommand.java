package com.amido.stacks.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateItemCommand extends MenuCommand {

  private UUID categoryId;
  private String name;
  private String description;
  private Double price = null;
  private Boolean available = null;
  private UUID itemId;

  public CreateItemCommand(
      String correlationId,
      UUID menuId,
      UUID categoryId,
      String name,
      String description,
      Double price,
      Boolean available) {
    super(OperationCode.CREATE_MENU_ITEM, correlationId, menuId);
    this.categoryId = categoryId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.available = available;
  }
}
