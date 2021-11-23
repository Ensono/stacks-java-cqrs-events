package com.amido.stacks.workloads.menu.events;

import com.amido.stacks.workloads.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class CategoryDeletedEvent extends CategoryEvent {

  private static final int EVENT_CODE = 203;

  public CategoryDeletedEvent(MenuCommand command, UUID categoryId) {
    super(command, categoryId);
  }

  @Override
  public int getEventCode() {
    return EVENT_CODE;
  }
}
