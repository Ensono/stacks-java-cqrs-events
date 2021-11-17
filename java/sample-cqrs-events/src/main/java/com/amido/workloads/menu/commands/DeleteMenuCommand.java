package com.amido.workloads.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Command for deleting Menu
 *
 * @author ArathyKrishna
 */
@Getter
@Setter
public class DeleteMenuCommand extends MenuCommand {

  private static final int OPERATION_CODE = 103;

  public DeleteMenuCommand(String correlationId, UUID menuId) {
    super(correlationId, menuId);
  }

  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
