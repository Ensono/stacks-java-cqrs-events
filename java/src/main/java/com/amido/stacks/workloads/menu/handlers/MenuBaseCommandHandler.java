package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.cqrs.handler.CommandHandler;
import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.MenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class MenuBaseCommandHandler<T extends MenuCommand> implements CommandHandler<T> {

  protected MenuService menuService;
  private ApplicationEventPublisherWithListener applicationEventPublisher;

  public MenuBaseCommandHandler(
      MenuService menuService, ApplicationEventPublisherWithListener applicationEventPublisher) {
    this.menuService = menuService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Optional<UUID> handle(T command) {

    Menu menu =
        menuService
            .findById(command.getMenuId().toString())
            .orElseThrow(() -> new MenuNotFoundException(command));

    var result = handleCommand(menu, command);

    publishEvents(raiseApplicationEvents(menu, command));

    return result;
  }

  protected void publishEvents(List<MenuEvent> menuEvents) {
    menuEvents.forEach(applicationEventPublisher::publish);
  }

  abstract Optional<UUID> handleCommand(Menu menu, T command);

  abstract List<MenuEvent> raiseApplicationEvents(Menu menu, T command);
}
