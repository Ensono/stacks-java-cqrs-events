package com.amido.stacks.menu.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
