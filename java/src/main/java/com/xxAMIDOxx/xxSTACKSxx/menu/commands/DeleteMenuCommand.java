package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

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

  public DeleteMenuCommand(String correlationId, UUID menuId) {
    super(OperationCode.DELETE_MENU, correlationId, menuId);
  }
}
