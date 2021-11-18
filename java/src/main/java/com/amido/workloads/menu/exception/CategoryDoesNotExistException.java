package com.amido.workloads.menu.exception;

import com.amido.workloads.menu.commands.MenuCommand;
import java.util.UUID;

public class CategoryDoesNotExistException extends MenuApiException {

  private static final int EXCEPTION_CODE = 11404;

  public CategoryDoesNotExistException(MenuCommand command, UUID categoryId) {
    super(
        String.format(
            "A category with the id '%s' does not exist for menu with id '%s'.",
            categoryId, command.getMenuId()),
        command);
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
