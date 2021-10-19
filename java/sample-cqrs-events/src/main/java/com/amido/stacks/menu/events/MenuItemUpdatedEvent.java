package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class MenuItemUpdatedEvent extends MenuItemEvent {

  public MenuItemUpdatedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, EventCode.MENU_ITEM_UPDATED, categoryId, itemId);
  }
}
