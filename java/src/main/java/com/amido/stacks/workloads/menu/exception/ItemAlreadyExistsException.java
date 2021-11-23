package com.amido.stacks.workloads.menu.exception;

import com.amido.stacks.workloads.menu.commands.MenuCommand;
import java.util.UUID;

public class ItemAlreadyExistsException extends MenuApiException {

  private static final int EXCEPTION_CODE = 12409;

  public ItemAlreadyExistsException(MenuCommand command, UUID categoryId, String name) {
    super(
        String.format(
            "An item with the name '%s' already exists for the category '%s' in menu with "
                + "id '%s'.",
            name, categoryId, command.getMenuId()),
        command);
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
