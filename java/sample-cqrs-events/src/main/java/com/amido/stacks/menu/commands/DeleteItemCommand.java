package com.amido.stacks.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** @author ArathyKrishna */
@Getter
@Setter
public class DeleteItemCommand extends MenuCommand {

  private UUID categoryId;
  private UUID itemId;

  public DeleteItemCommand(String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    super(OperationCode.DELETE_MENU_ITEM, correlationId, menuId);
    this.categoryId = categoryId;
    this.itemId = itemId;
  }
}
