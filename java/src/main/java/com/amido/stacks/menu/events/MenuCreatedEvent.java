package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

public class MenuCreatedEvent extends MenuEvent {

  public MenuCreatedEvent(MenuCommand command, UUID menuId) {
    super(command, EventCode.MENU_CREATED, menuId);
  }
}
