package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;

public class MenuUpdatedEvent extends MenuEvent {

  public MenuUpdatedEvent(MenuCommand command) {
    super(command, EventCode.MENU_UPDATED);
  }
}
