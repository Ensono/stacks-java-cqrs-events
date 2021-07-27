package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import java.util.UUID;

public class CategoryUpdatedEvent extends CategoryEvent {

  public CategoryUpdatedEvent(MenuCommand command, UUID categoryId) {
    super(command, EventCode.CATEGORY_UPDATED, categoryId);
  }
}
