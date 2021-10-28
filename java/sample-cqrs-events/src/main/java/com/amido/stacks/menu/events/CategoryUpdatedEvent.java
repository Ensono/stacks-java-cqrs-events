package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

public class CategoryUpdatedEvent extends CategoryEvent {

  private static final int EVENT_CODE = 202;

  public CategoryUpdatedEvent(MenuCommand command, UUID categoryId) {
    super(command, categoryId);
  }

  @Override
  public int getEventCode() {
    return EVENT_CODE;
  }
}
