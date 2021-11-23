package com.amido.stacks.workloads.menu.events;

import com.amido.stacks.workloads.menu.commands.MenuCommand;
import java.util.UUID;

public class MenuItemCreatedEvent extends MenuItemEvent {

  private static final int EVENT_CODE = 301;

  public MenuItemCreatedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, categoryId, itemId);
  }

  @Override
  public int getEventCode() {
    return EVENT_CODE;
  }
}
