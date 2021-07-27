package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CreateItemHandler extends MenuBaseCommandHandler<CreateItemCommand> {

  public CreateItemHandler(
      MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
    super(menuRepository, applicationEventPublisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, CreateItemCommand command) {
    command.setItemId(UUID.randomUUID());
    Category category = addItem(getCategory(menu, command), command);
    menuRepository.save(menu.addOrUpdateCategory(category));
    return Optional.of(command.getItemId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, CreateItemCommand command) {
    return Arrays.asList(
        new MenuUpdatedEvent(command),
        new CategoryUpdatedEvent(command, command.getCategoryId()),
        new MenuItemCreatedEvent(command, command.getCategoryId(), command.getItemId()));
  }

  Category getCategory(Menu menu, CreateItemCommand command) {
    Optional<Category> existing = Optional.empty();

    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing =
          menu.getCategories().stream()
              .filter(c -> c.getId().equals(command.getCategoryId().toString()))
              .findFirst();
    }
    return existing.orElseThrow(
        () -> new CategoryDoesNotExistException(command, command.getCategoryId()));
  }

  Category addItem(Category category, CreateItemCommand command) {
    List<Item> items = category.getItems() == null ? new ArrayList<>() : category.getItems();

    if (items.stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
      throw new ItemAlreadyExistsException(command, command.getCategoryId(), command.getName());
    } else {
      Item item =
          new Item(
              command.getItemId().toString(),
              command.getName(),
              command.getDescription(),
              command.getPrice(),
              command.getAvailable());
      category.getItems().add(item);
      return category;
    }
  }
}
