package com.amido.stacks.menu.events;

import com.amido.stacks.core.messaging.event.ApplicationEvent;
import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;

public abstract class MenuEvent extends ApplicationEvent {

  private UUID menuId;

  public MenuEvent(final MenuCommand command, final EventCode eventCode, final UUID menuId) {
    super(command.getOperationCode(), command.getCorrelationId(), eventCode.getCode());
    this.menuId = menuId;
  }

  public MenuEvent(final MenuCommand command, final EventCode eventCode) {
    super(command.getOperationCode(), command.getCorrelationId(), eventCode.getCode());
    this.menuId = command.getMenuId();
  }

  public UUID getMenuId() {
    return menuId;
  }

  @Override
  public String toString() {
    return "MenuEvent{" + "menuId=" + menuId + "} " + super.toString();
  }
}
