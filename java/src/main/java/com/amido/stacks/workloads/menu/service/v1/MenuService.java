package com.amido.stacks.workloads.menu.service.v1;

import com.amido.stacks.workloads.menu.commands.CreateMenuCommand;
import com.amido.stacks.workloads.menu.commands.UpdateMenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuAlreadyExistsException;
import com.amido.stacks.workloads.menu.mappers.cqrs.CreateMenuCommandMapper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;

  private final CreateMenuCommandMapper createMenuCommandMapper;

  public Optional<Menu> create(Menu menu) {

    menuRepository.save(menu);

    return Optional.of(menu);
  }

  public void verifyMenuNotAlreadyExisting(CreateMenuCommand command) {
    Menu menu = createMenuCommandMapper.fromDto(command);

    Page<Menu> existing =
        menuRepository.findAllByRestaurantIdAndName(
            menu.getRestaurantId(), menu.getName(), PageRequest.of(0, 1));

    if (!existing.getContent().isEmpty()
        && existing.get().anyMatch(m -> m.getName().equals(menu.getName()))) {
      throw new MenuAlreadyExistsException(
          command, UUID.fromString(menu.getRestaurantId()), menu.getName());
    }
  }

  public Optional<Menu> findById(String id) {
    return menuRepository.findById(id);
  }

  public void delete(Menu menu) {
    menuRepository.delete(menu);
  }

  public void update(Menu menu, UpdateMenuCommand command) {

    menu.setName(command.getName());
    menu.setDescription(command.getDescription());
    menu.setEnabled(command.getEnabled());

    menuRepository.save(menu);
  }
}
