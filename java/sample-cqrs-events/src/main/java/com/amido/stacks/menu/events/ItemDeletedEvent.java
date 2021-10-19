package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class ItemDeletedEvent extends MenuItemEvent {

  public ItemDeletedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, EventCode.MENU_ITEM_DELETED, categoryId, itemId);
  }
}
