package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.cqrs.handler.CommandHandler;
import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.CreateMenuCommand;
import com.amido.stacks.workloads.menu.events.MenuCreatedEvent;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CreateMenuHandler implements CommandHandler<CreateMenuCommand> {

  private final MenuService menuService;

  private ApplicationEventPublisherWithListener applicationEventPublisher;

  public CreateMenuHandler(
      MenuService menuService, ApplicationEventPublisherWithListener applicationEventPublisher) {
    this.menuService = menuService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Optional<UUID> handle(CreateMenuCommand command) {

    final UUID id = UUID.randomUUID();
    menuService.save(command, id);
    applicationEventPublisher.publish(new MenuCreatedEvent(command, id));

    return Optional.of(id);
  }
}
