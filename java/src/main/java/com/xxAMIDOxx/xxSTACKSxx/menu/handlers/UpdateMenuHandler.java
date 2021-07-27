package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UpdateMenuHandler extends MenuBaseCommandHandler<UpdateMenuCommand> {

  public UpdateMenuHandler(
      MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
    super(menuRepository, applicationEventPublisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, UpdateMenuCommand command) {
    menu.setName(command.getName());
    menu.setDescription(command.getDescription());
    menu.setEnabled(command.getEnabled());
    menuRepository.save(menu);
    return Optional.of(command.getMenuId());
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, UpdateMenuCommand command) {
    return Collections.singletonList(new MenuUpdatedEvent(command));
  }
}
