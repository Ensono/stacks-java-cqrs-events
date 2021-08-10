package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;

/** @author ArathyKrishna */
public class MenuDeletedEvent extends MenuEvent {

  public MenuDeletedEvent(MenuCommand command) {
    super(command, EventCode.MENU_DELETED);
  }
}
