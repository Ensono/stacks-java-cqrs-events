package com.amido.stacks.menu.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateItemCommand extends MenuCommand {

  private static final int OPERATION_CODE = 301;

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
    super(correlationId, menuId);
    this.categoryId = categoryId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.available = available;
  }


  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
