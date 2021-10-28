package com.amido.stacks.menu.commands;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMenuCommand extends MenuCommand {

  private static final int OPERATION_CODE = 102;

  private String name;
  private String description;
  private Boolean enabled;

  public UpdateMenuCommand(
      String correlationId, UUID menuId, String name, String description, Boolean enabled) {
    super(correlationId, menuId);
    this.name = name;
    this.description = description;
    this.enabled = enabled;
  }

  @Override
  public int getOperationCode() {
    return OPERATION_CODE;
  }
}
