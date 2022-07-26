package com.amido.stacks.workloads.menu.events;

import com.amido.stacks.core.messaging.event.ApplicationEvent;
import com.amido.stacks.workloads.menu.commands.MenuCommand;
import java.util.UUID;

public abstract class MenuEvent extends ApplicationEvent {

  private UUID menuId;

  protected MenuEvent(MenuCommand command, UUID menuId) {
    super(command.getOperationCode(), command.getCorrelationId());
    this.menuId = menuId;
  }

  protected MenuEvent(MenuCommand command) {
    super(command.getOperationCode(), command.getCorrelationId());
    this.menuId = command.getMenuId();
  }

  public UUID getMenuId() {
    return menuId;
  }

  @Override
  public abstract int getEventCode();

  @Override
  public String toString() {
    return "MenuEvent{" + "menuId=" + menuId + "} " + super.toString();
  }
}
