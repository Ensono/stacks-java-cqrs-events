package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.cqrs.handler.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class MenuBaseCommandHandler<T extends MenuCommand> implements CommandHandler<T> {

  protected MenuRepository menuRepository;

  private ApplicationEventPublisher applicationEventPublisher;

  public MenuBaseCommandHandler(
      MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
    this.menuRepository = menuRepository;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Optional<UUID> handle(T command) {

    Menu menu =
        menuRepository
            .findById(command.getMenuId().toString())
            .orElseThrow(() -> new MenuNotFoundException(command));

    var result = handleCommand(menu, command);

    publishEvents(raiseApplicationEvents(menu, command));

    return result;
  }

  private void publishEvents(List<MenuEvent> menuEvents) {
    menuEvents.forEach(applicationEventPublisher::publish);
  }

  abstract Optional<UUID> handleCommand(Menu menu, T command);

  abstract List<MenuEvent> raiseApplicationEvents(Menu menu, T command);

  /**
   * find a category for the id provided
   *
   * @param menu menu object
   * @param categoryId category id
   * @return category if found else optional.empty
   */
  public Optional<Category> findCategory(Menu menu, UUID categoryId) {
    Optional<Category> existing = Optional.empty();
    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing =
          menu.getCategories().stream()
              .filter(c -> c.getId().equals(categoryId.toString()))
              .findFirst();
    }
    return existing;
  }

  public Optional<Item> findItem(Category category, UUID itemId) {
    Optional<Item> existing = Optional.empty();

    if (category.getItems() != null && !category.getItems().isEmpty()) {
      existing =
          category.getItems().stream().filter(t -> t.getId().equals(itemId.toString())).findFirst();
    }
    return existing;
  }
}
