package com.amido.workloads.menu.exception;

import com.amido.workloads.menu.commands.MenuCommand;

public class CategoryAlreadyExistsException extends MenuApiException {

  private static final int EXCEPTION_CODE = 11409;

  public CategoryAlreadyExistsException(MenuCommand command, String name) {
    super(
        String.format(
            "A category with the name '%s' already exists for the menu with id '%s'.",
            name, command.getMenuId()),
        command);
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
