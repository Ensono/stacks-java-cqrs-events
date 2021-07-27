package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.event.ApplicationEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
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
