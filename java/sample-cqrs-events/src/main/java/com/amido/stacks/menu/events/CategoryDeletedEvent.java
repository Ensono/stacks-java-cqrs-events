package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
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
