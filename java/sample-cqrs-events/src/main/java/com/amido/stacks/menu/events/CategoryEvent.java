package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class CategoryEvent extends MenuEvent {
  private final UUID categoryId;

  public CategoryEvent(MenuCommand menuCommand, UUID categoryId) {
    super(menuCommand, menuCommand.getMenuId());
    this.categoryId = categoryId;
  }
}
