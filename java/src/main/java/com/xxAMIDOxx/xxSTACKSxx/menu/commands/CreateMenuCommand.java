package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMenuCommand extends MenuCommand {

  private String name;
  private String description;
  private UUID restaurantId;
  private Boolean enabled;

  public CreateMenuCommand(
      String correlationId, String name, String description, UUID restaurantId, Boolean enabled) {
    super(OperationCode.CREATE_MENU, correlationId, null);
    this.name = name;
    this.description = description;
    this.restaurantId = restaurantId;
    this.enabled = enabled;
  }
}
