package com.amido.stacks.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMenuCommand extends MenuCommand {

  private String name;
  private String description;
  private Boolean enabled;

  public UpdateMenuCommand(
      String correlationId, UUID menuId, String name, String description, Boolean enabled) {
    super(OperationCode.UPDATE_MENU, correlationId, menuId);
    this.name = name;
    this.description = description;
    this.enabled = enabled;
  }
}
