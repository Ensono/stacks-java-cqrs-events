package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.DeleteItemCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryUpdatedEvent;
import com.amido.stacks.workloads.menu.events.ItemDeletedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
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
public class DeleteItemHandler extends MenuBaseCommandHandler<DeleteItemCommand> {

  protected ItemService itemService;

  public DeleteItemHandler(
      MenuService menuService,
      ApplicationEventPublisherWithListener applicationEventPublisher,
      ItemService itemService) {
    super(menuService, applicationEventPublisher);
    this.itemService = itemService;
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, DeleteItemCommand command) {

    itemService.delete(menu, command);

    publishEvents(raiseApplicationEvents(menu, command));

    return Optional.empty();
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, DeleteItemCommand command) {

    return Arrays.asList(
        new ItemDeletedEvent(command, command.getCategoryId(), command.getItemId()),
        new CategoryUpdatedEvent(command, command.getCategoryId()),
        new MenuUpdatedEvent(command));
  }
}
