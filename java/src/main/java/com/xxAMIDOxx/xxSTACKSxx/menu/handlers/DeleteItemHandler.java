package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.DeleteItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.ItemDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemDoesNotExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
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
      MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
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
