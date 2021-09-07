package com.amido.stacks.menu.handlers;

import com.amido.stacks.core.cqrs.handler.CommandHandler;
import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.menu.commands.MenuCommand;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.events.MenuEvent;
import com.amido.stacks.menu.exception.MenuNotFoundException;
import com.amido.stacks.menu.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class MenuBaseCommandHandler<T extends MenuCommand> implements CommandHandler<T> {

  protected MenuRepository menuRepository;

  private ApplicationEventPublisherWithListener applicationEventPublisher;

  public MenuBaseCommandHandler(
      MenuRepository menuRepository,
      ApplicationEventPublisherWithListener applicationEventPublisher) {
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
