package com.amido.workloads.menu.events;

import com.amido.workloads.menu.commands.MenuCommand;
import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

  private static final int EVENT_CODE = 201;

  public CategoryCreatedEvent(MenuCommand command, UUID categoryId) {
    super(command, categoryId);
  }

  @Override
  public int getEventCode() {
    return 0;
  }
}
