package com.amido.stacks.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.menu.commands.DeleteItemCommand;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.events.CategoryUpdatedEvent;
import com.amido.stacks.menu.events.ItemDeletedEvent;
import com.amido.stacks.menu.events.MenuEvent;
import com.amido.stacks.menu.events.MenuUpdatedEvent;
import com.amido.stacks.menu.exception.CategoryDoesNotExistException;
import com.amido.stacks.menu.exception.ItemDoesNotExistsException;
import com.amido.stacks.menu.repository.MenuRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** @author ArathyKrishna */
@Component
public class DeleteItemHandler extends MenuBaseCommandHandler<DeleteItemCommand> {

  public DeleteItemHandler(
      MenuRepository menuRepository,
      ApplicationEventPublisherWithListener applicationEventPublisher) {
    super(menuRepository, applicationEventPublisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, DeleteItemCommand command) {
    Category category = getCategory(menu, command);
    Item item = getItem(category, command);

    List<Item> itemList =
        category.getItems().stream()
            .filter(t -> !Objects.equals(t, item))
            .collect(Collectors.toList());
    category.setItems(!itemList.isEmpty() ? itemList : Collections.emptyList());

    menuRepository.save(menu.addOrUpdateCategory(category));

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

  Category getCategory(Menu menu, DeleteItemCommand command) {
    return findCategory(menu, command.getCategoryId())
        .orElseThrow(() -> new CategoryDoesNotExistException(command, command.getCategoryId()));
  }

  Item getItem(Category category, DeleteItemCommand command) {
    return findItem(category, command.getItemId())
        .orElseThrow(
            () ->
                new ItemDoesNotExistsException(
                    command, command.getCategoryId(), command.getItemId()));
  }
}
