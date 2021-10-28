package com.amido.stacks.menu.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
