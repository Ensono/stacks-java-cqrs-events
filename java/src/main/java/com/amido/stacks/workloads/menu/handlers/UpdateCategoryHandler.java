package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.UpdateCategoryCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryUpdatedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.events.MenuUpdatedEvent;
import com.amido.stacks.workloads.menu.service.v1.CategoryService;

import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** @author ArathyKrishna */
@Component
public class UpdateCategoryHandler extends MenuBaseCommandHandler<UpdateCategoryCommand> {

  protected CategoryService categoryService;

  public UpdateCategoryHandler(
      MenuService menuService,
      ApplicationEventPublisherWithListener applicationEventPublisher,
      CategoryService categoryService) {
    super(menuService, applicationEventPublisher);

    this.categoryService = categoryService;
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, UpdateCategoryCommand command) {

    categoryService.update(menu, command);

    publishEvents(raiseApplicationEvents(menu, command));

    return Optional.of(command.getCategoryId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, UpdateCategoryCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command), new CategoryUpdatedEvent(command, command.getCategoryId()));
  }
}
