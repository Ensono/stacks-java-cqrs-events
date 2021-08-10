package com.amido.stacks.menu.handlers;

import com.amido.stacks.core.cqrs.handler.CommandHandler;
import com.amido.stacks.core.messaging.publish.ApplicationEventPublisher;
import com.amido.stacks.menu.commands.CreateMenuCommand;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.events.MenuCreatedEvent;
import com.amido.stacks.menu.exception.MenuAlreadyExistsException;
import com.amido.stacks.menu.repository.MenuRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateMenuHandler implements CommandHandler<CreateMenuCommand> {

  protected MenuRepository menuRepository;

  private ApplicationEventPublisher applicationEventPublisher;

  public CreateMenuHandler(
      MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
    this.menuRepository = menuRepository;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Optional<UUID> handle(CreateMenuCommand command) {

    verifyMenuNotAlreadyExisting(command);

    final UUID id = UUID.randomUUID();
    final Menu menu =
        new Menu(
            id.toString(),
            command.getRestaurantId().toString(),
            command.getName(),
            command.getDescription(),
            new ArrayList<>(),
            command.getEnabled());

    menuRepository.save(menu);

    applicationEventPublisher.publish(new MenuCreatedEvent(command, id));

    return Optional.of(id);
  }

  protected void verifyMenuNotAlreadyExisting(CreateMenuCommand command) {
    Page<Menu> existing =
        menuRepository.findAllByRestaurantIdAndName(
            command.getRestaurantId().toString(), command.getName(), PageRequest.of(0, 1));
    if (!existing.getContent().isEmpty()
        && existing.get().anyMatch(m -> m.getName().equals(command.getName()))) {
      throw new MenuAlreadyExistsException(command, command.getRestaurantId(), command.getName());
    }
  }
}
