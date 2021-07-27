package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** @author ArathyKrishna */
@Getter
@Setter
public class DeleteCategoryCommand extends MenuCommand {

  private UUID categoryId;

  public DeleteCategoryCommand(String correlationId, UUID menuId, UUID categoryId) {
    super(OperationCode.DELETE_CATEGORY, correlationId, menuId);
    this.categoryId = categoryId;
  }
}
