package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

/** @author ArathyKrishna */
public class CategoryDeletedEvent extends CategoryEvent {

  public CategoryDeletedEvent(MenuCommand command, UUID categoryId) {
    super(command, EventCode.CATEGORY_DELETED, categoryId);
  }
}
