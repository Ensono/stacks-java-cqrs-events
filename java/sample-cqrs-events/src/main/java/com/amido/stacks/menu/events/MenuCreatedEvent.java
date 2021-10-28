package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

public class MenuCreatedEvent extends MenuEvent {

  private static final int EVENT_CODE = 101;

  public MenuCreatedEvent(MenuCommand command, UUID menuId) {
    super(command, menuId);
  }

  @Override
  public int getEventCode() {
    return EVENT_CODE;
  }
}
