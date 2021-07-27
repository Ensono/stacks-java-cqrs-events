package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class ItemDoesNotExistsException extends MenuApiException {

  public ItemDoesNotExistsException(MenuCommand command, UUID categoryId, UUID itemId) {
    super(
        String.format(
            "An item with the id '%s' does not exists for category with the id '%s' and for menu with id '%s'.",
            itemId, categoryId, command.getMenuId()),
        ExceptionCode.ITEM_DOES_NOT_EXIST,
        command);
  }
}
