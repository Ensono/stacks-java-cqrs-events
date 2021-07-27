package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import java.util.UUID;

public class MenuAlreadyExistsException extends MenuApiException {

  public MenuAlreadyExistsException(MenuCommand command, UUID restaurantId, String name) {
    super(
        String.format(
            "A Menu with the name '%s' already exists for the restaurant with id '%s'.",
            name, restaurantId),
        ExceptionCode.MENU_ALREADY_EXISTS,
        command);
  }
}
