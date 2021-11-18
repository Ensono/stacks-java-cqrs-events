package com.amido.workloads.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

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
