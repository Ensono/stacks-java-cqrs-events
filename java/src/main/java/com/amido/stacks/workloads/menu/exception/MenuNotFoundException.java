package com.amido.stacks.workloads.menu.exception;

import com.amido.stacks.workloads.menu.commands.MenuCommand;

public class MenuNotFoundException extends MenuApiException {

  private static final int EXCEPTION_CODE = 10404;

  public MenuNotFoundException(MenuCommand command) {
    super(String.format("A menu with id '%s' does not exist.", command.getMenuId()), command);
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
