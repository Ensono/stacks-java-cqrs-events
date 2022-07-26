package com.amido.stacks.workloads.menu.events;

import com.amido.stacks.workloads.menu.commands.MenuCommand;
import java.util.UUID;

public abstract class MenuItemEvent extends CategoryEvent {
  private final UUID itemId;

  protected MenuItemEvent(MenuCommand command, UUID categoryId, UUID itemId) {
    super(command, categoryId);
    this.itemId = itemId;
  }

  public UUID getItemId() {
    return itemId;
  }

  @Override
  public String toString() {
    return "MenuItemEvent{" + "itemId=" + itemId + "} " + super.toString();
  }
}
