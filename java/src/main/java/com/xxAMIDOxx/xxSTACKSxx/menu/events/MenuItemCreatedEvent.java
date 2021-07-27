package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import java.util.UUID;

public class MenuItemCreatedEvent extends MenuItemEvent {
  public MenuItemCreatedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, EventCode.MENU_ITEM_CREATED, categoryId, itemId);
  }
}
