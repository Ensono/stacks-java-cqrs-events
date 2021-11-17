package com.amido.workloads.menu.exception;

import com.amido.workloads.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class ItemDoesNotExistsException extends MenuApiException {

  private static final int EXCEPTION_CODE = 12404;

  public ItemDoesNotExistsException(MenuCommand command, UUID categoryId, UUID itemId) {
    super(
        String.format(
            "An item with the id '%s' does not exists for category with the id '%s' and for menu with id '%s'.",
            itemId, categoryId, command.getMenuId()),
        command);
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
