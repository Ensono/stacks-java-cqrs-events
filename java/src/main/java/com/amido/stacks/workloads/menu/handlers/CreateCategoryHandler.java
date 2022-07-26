package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.CreateCategoryCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryCreatedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.events.MenuUpdatedEvent;
import com.amido.stacks.workloads.menu.service.v1.CategoryService;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CreateCategoryHandler extends MenuBaseCommandHandler<CreateCategoryCommand> {

  protected CategoryService categoryService;

  public CreateCategoryHandler(
      MenuService menuService,
      ApplicationEventPublisherWithListener applicationEventPublisher,
      CategoryService categoryService) {
    super(menuService, applicationEventPublisher);
    this.categoryService = categoryService;
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, CreateCategoryCommand command) {
    categoryService.create(menu, command);
    publishEvents(raiseApplicationEvents(menu, command));
    return Optional.of(command.getCategoryId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, CreateCategoryCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command), new CategoryCreatedEvent(command, command.getCategoryId()));
  }
}
