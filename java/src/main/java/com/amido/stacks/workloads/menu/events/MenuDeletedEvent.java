package com.amido.stacks.workloads.menu.events;

import com.amido.stacks.workloads.menu.commands.MenuCommand;

public class MenuDeletedEvent extends MenuEvent {

  private static final int EVENT_CODE = 103;

  public MenuDeletedEvent(MenuCommand command) {
    super(command);
  }

  @Override
  public int getEventCode() {
    return EVENT_CODE;
  }
}
