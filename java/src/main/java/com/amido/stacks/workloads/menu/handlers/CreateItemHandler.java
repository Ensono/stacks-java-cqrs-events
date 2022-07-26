package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.CreateItemCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryUpdatedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.events.MenuItemCreatedEvent;
import com.amido.stacks.workloads.menu.events.MenuUpdatedEvent;
import com.amido.stacks.workloads.menu.service.v1.ItemService;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CreateItemHandler extends MenuBaseCommandHandler<CreateItemCommand> {

  protected ItemService itemService;

  public CreateItemHandler(
      MenuService menuService,
      ApplicationEventPublisherWithListener applicationEventPublisher,
      ItemService itemService) {
    super(menuService, applicationEventPublisher);
    this.itemService = itemService;
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, CreateItemCommand command) {
    itemService.create(menu, command);

    publishEvents(raiseApplicationEvents(menu, command));

    return Optional.of(command.getItemId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, CreateItemCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command),
        new CategoryUpdatedEvent(command, command.getCategoryId()),
        new MenuItemCreatedEvent(command, command.getCategoryId(), command.getItemId()));
  }
}
