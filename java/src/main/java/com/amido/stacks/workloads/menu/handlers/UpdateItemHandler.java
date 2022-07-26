package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.UpdateItemCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryUpdatedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.events.MenuItemUpdatedEvent;
import com.amido.stacks.workloads.menu.events.MenuUpdatedEvent;
import com.amido.stacks.workloads.menu.service.v1.ItemService;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** @author ArathyKrishna */
@Component
public class UpdateItemHandler extends MenuBaseCommandHandler<UpdateItemCommand> {

  protected ItemService itemService;

  public UpdateItemHandler(
      MenuService menuService,
      ApplicationEventPublisherWithListener applicationEventPublisher,
      ItemService itemService) {
    super(menuService, applicationEventPublisher);
    this.itemService = itemService;
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, UpdateItemCommand command) {

    itemService.update(menu, command);
    publishEvents(raiseApplicationEvents(menu, command));

    publishEvents(raiseApplicationEvents(menu, command));

    return Optional.of(command.getItemId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, UpdateItemCommand command) {
    return Arrays.asList(
        new MenuItemUpdatedEvent(command, command.getCategoryId(), command.getItemId()),
        new CategoryUpdatedEvent(command, command.getCategoryId()),
        new CategoryUpdatedEvent(command, command.getCategoryId()),
        new MenuUpdatedEvent(command));
  }
}
