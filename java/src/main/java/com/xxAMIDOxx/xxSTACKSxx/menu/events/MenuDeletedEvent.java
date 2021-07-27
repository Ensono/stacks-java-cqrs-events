package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;

/** @author ArathyKrishna */
public class MenuDeletedEvent extends MenuEvent {

  public MenuDeletedEvent(MenuCommand command) {
    super(command, EventCode.MENU_DELETED);
  }
}
