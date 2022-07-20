package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.DeleteCategoryCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryDeletedEvent;
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
public class DeleteCategoryHandler extends MenuBaseCommandHandler<DeleteCategoryCommand> {

  private final CategoryService categoryService;

  public DeleteCategoryHandler(
      ApplicationEventPublisherWithListener applicationEventPublisher,
      MenuService menuService,
      CategoryService categoryService) {
    super(applicationEventPublisher, menuService);
    this.categoryService = categoryService;
  }

  Optional<UUID> handleCommand(Menu menu, DeleteCategoryCommand command) {
    categoryService.delete(menu, command);
    publishEvents(raiseApplicationEvents(menu, command));
    return Optional.empty();
  }

  List<MenuEvent> raiseApplicationEvents(Menu menu, DeleteCategoryCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command), new CategoryDeletedEvent(command, command.getCategoryId()));
  }
}
