package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class ItemDeletedEvent extends MenuItemEvent {

  private static final int EVENT_CODE = 303;

  public ItemDeletedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, categoryId, itemId);
  }
  @Override
  public int getEventCode() {
    return EVENT_CODE;
  }
}
