package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.cqrs.handler.CommandHandler;
import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.CreateMenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.MenuCreatedEvent;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateMenuHandler implements CommandHandler<CreateMenuCommand> {

  private final MenuService menuService;

  private final ApplicationEventPublisherWithListener applicationEventPublisher;

  @Override
  public Optional<UUID> handle(CreateMenuCommand command) {
    UUID id = UUID.randomUUID();
    Menu menu =
        new Menu(
            id.toString(),
            command.getRestaurantId().toString(),
            command.getName(),
            command.getDescription(),
            new ArrayList<>(),
            command.getEnabled());

    menuService.verifyMenuNotAlreadyExisting(command);

    menuService.create(menu);

    applicationEventPublisher.publish(new MenuCreatedEvent(command, id));

    return Optional.of(id);
  }
}
