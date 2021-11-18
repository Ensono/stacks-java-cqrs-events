package com.amido.workloads.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** @author ArathyKrishna */
@Getter
@Setter
public class DeleteCategoryCommand extends MenuCommand {

  private static final int OPERATION_CODE = 203;

  private UUID categoryId;

  public DeleteCategoryCommand(String correlationId, UUID menuId, UUID categoryId) {
    super(correlationId, menuId);
    this.categoryId = categoryId;
  }

  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
