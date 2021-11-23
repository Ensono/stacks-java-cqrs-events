package com.amido.stacks.workloads.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** @author ArathyKrishna */
@Getter
@Setter
public class UpdateCategoryCommand extends MenuCommand {

  private static final int OPERATION_CODE = 202;

  private String name;
  private String description;
  private UUID categoryId;

  public UpdateCategoryCommand(
      String correlationId, UUID menuId, UUID categoryId, String name, String description) {
    super(correlationId, menuId);
    this.name = name;
    this.description = description;
    this.categoryId = categoryId;
  }

  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
