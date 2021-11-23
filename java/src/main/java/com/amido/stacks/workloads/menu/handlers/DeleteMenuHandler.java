package com.amido.stacks.workloads.menu.handlers;

import com.amido.stacks.core.messaging.publish.ApplicationEventPublisherWithListener;
import com.amido.stacks.workloads.menu.commands.DeleteMenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.events.MenuDeletedEvent;
import com.amido.stacks.workloads.menu.events.MenuEvent;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Handler for deleting menu
 *
 * @author ArathyKrishna
 */
@Component
public class DeleteMenuHandler extends MenuBaseCommandHandler<DeleteMenuCommand> {

  public DeleteMenuHandler(
      MenuRepository menuRepository,
      ApplicationEventPublisherWithListener applicationEventPublisher) {
    super(menuRepository, applicationEventPublisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, DeleteMenuCommand command) {
    menuRepository.delete(menu);
    publishEvents(raiseApplicationEvents(menu, command));
    return Optional.empty();
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, DeleteMenuCommand command) {
    return Collections.singletonList(new MenuDeletedEvent(command));
  }
}
