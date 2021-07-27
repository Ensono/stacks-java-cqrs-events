package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** @author ArathyKrishna */
@Component
public class UpdateCategoryHandler extends MenuBaseCommandHandler<UpdateCategoryCommand> {

  public UpdateCategoryHandler(MenuRepository repository, ApplicationEventPublisher publisher) {
    super(repository, publisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, UpdateCategoryCommand command) {
    menu.addOrUpdateCategory(updateCategory(menu, command));
    menuRepository.save(menu);
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
