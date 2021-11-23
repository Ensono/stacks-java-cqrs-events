package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.UpdateCategoryCommand;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.CategoryUpdatedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.events.MenuUpdatedEvent;
import com.amido.stacks.workloads.menu.exception.CategoryAlreadyExistsException;
import com.amido.stacks.workloads.menu.exception.CategoryDoesNotExistException;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** @author ArathyKrishna */
@Component
public class UpdateCategoryHandler extends MenuBaseCommandHandler<UpdateCategoryCommand> {

  public UpdateCategoryHandler(
      MenuRepository repository, ApplicationEventPublisherWithListener publisher) {
    super(repository, publisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, UpdateCategoryCommand command) {
    menu.addOrUpdateCategory(updateCategory(menu, command));
    menuRepository.save(menu);
    publishEvents(raiseApplicationEvents(menu, command));
    return Optional.of(command.getCategoryId());
  }

  /**
   * if the request is to update the name and description of a category If there is a category with
   * the same name but only updating the description then allow that else throw a category already
   * exists exception if a category with the same name doesn't exits then update the requested
   * category.
   *
   * @param menu menu
   * @param command update category request
   * @return category
   */
  Category updateCategory(Menu menu, UpdateCategoryCommand command) {
    Category category = getCategory(menu, command);
    menu.getCategories()
        .forEach(
            t -> {
              if (t.getName().equalsIgnoreCase(command.getName())) {
                if (t.getId().equalsIgnoreCase(command.getCategoryId().toString())) {
                  category.setDescription(command.getDescription());
                } else {
                  throw new CategoryAlreadyExistsException(command, command.getName());
                }
              } else {
                category.setDescription(command.getDescription());
                category.setName(command.getName());
              }
            });

    return category;
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, UpdateCategoryCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command), new CategoryUpdatedEvent(command, command.getCategoryId()));
  }

  Category getCategory(Menu menu, UpdateCategoryCommand command) {
    return findCategory(menu, command.getCategoryId())
        .orElseThrow(() -> new CategoryDoesNotExistException(command, command.getCategoryId()));
  }
}
