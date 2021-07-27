package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CreateCategoryHandler extends MenuBaseCommandHandler<CreateCategoryCommand> {

  public CreateCategoryHandler(
      MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
    super(menuRepository, applicationEventPublisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, CreateCategoryCommand command) {
    command.setCategoryId(UUID.randomUUID());
    menu.setCategories(addCategory(menu, command));
    menuRepository.save(menu);
    return Optional.of(command.getCategoryId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, CreateCategoryCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command), new CategoryCreatedEvent(command, command.getCategoryId()));
  }

  List<Category> addCategory(Menu menu, CreateCategoryCommand command) {
    List<Category> categories =
        menu.getCategories() == null ? new ArrayList<>() : menu.getCategories();

    if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
      throw new CategoryAlreadyExistsException(command, command.getName());
    } else {
      categories.add(
          new Category(
              command.getCategoryId().toString(),
              command.getName(),
              command.getDescription(),
              new ArrayList<>()));
      return categories;
    }
  }
}
