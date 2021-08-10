package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

  public CategoryCreatedEvent(MenuCommand command, UUID categoryId) {
    super(command, EventCode.CATEGORY_CREATED, categoryId);
  }
}
