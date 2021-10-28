package com.amido.stacks.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** @author ArathyKrishna */
@Getter
@Setter
public class DeleteItemCommand extends MenuCommand {

  private static final int OPERATION_CODE = 303;

  private UUID categoryId;
  private UUID itemId;

  public DeleteItemCommand(String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    super(correlationId, menuId);
    this.categoryId = categoryId;
    this.itemId = itemId;
  }

  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
