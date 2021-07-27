package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;

public class MenuUpdatedEvent extends MenuEvent {

  public MenuUpdatedEvent(MenuCommand command) {
    super(command, EventCode.MENU_UPDATED);
  }
}
