package com.amido.stacks.menu.events;

import com.amido.stacks.menu.commands.MenuCommand;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public abstract class CategoryEvent extends MenuEvent {
  private final UUID categoryId;

  public CategoryEvent(MenuCommand menuCommand, UUID categoryId) {
    super(menuCommand, menuCommand.getMenuId());
    this.categoryId = categoryId;
  }
}
