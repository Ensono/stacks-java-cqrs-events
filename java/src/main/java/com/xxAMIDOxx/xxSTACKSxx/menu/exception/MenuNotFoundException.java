package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;

public class MenuNotFoundException extends MenuApiException {

  public MenuNotFoundException(MenuCommand command) {
    super(
        String.format("A menu with id '%s' does not exist.", command.getMenuId()),
        ExceptionCode.MENU_DOES_NOT_EXIST,
        command);
  }
}
