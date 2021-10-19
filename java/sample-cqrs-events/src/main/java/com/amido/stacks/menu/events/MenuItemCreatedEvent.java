package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

public class MenuItemCreatedEvent extends MenuItemEvent {
  public MenuItemCreatedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, EventCode.MENU_ITEM_CREATED, categoryId, itemId);
  }
}
