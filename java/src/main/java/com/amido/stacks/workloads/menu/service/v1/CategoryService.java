package com.amido.stacks.workloads.menu.service.v1;

import com.amido.stacks.workloads.menu.commands.CreateCategoryCommand;
import com.amido.stacks.workloads.menu.commands.DeleteCategoryCommand;
import com.amido.stacks.workloads.menu.commands.UpdateCategoryCommand;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.CategoryDoesNotExistException;
import com.amido.stacks.workloads.menu.mappers.cqrs.CreateCategoryCommandMapper;
import com.amido.stacks.workloads.menu.mappers.cqrs.UpdateCategoryCommandMapper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.menu.service.v1.utility.MenuHelperService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final MenuHelperService menuHelperService;
  private final MenuRepository menuRepository;
  private final CreateCategoryCommandMapper createCategoryCommandMapper;
  private final UpdateCategoryCommandMapper updateCategoryCommandMapper;

  public Optional<UUID> create(Menu menu, CreateCategoryCommand command) {

    menuHelperService.verifyCategoryNameNotAlreadyExisting(
        command, menu, command.getCategoryId(), command.getName());

    UUID id = UUID.randomUUID();

    Category category = createCategoryCommandMapper.fromDto(command);
    category.setId(id.toString());
    command.setCategoryId(id);

    menuHelperService.addOrUpdateCategory(menu, category);
    menuRepository.save(menu);

    return Optional.of(command.getCategoryId());
  }

  public void delete(Menu menu, DeleteCategoryCommand command) {

    Optional<Category> optCategory =
        menu.getCategories().stream()
            .filter(c -> command.getCategoryId().toString().equals(c.getId()))
            .findFirst();

    if (optCategory.isEmpty()) {
      throw new CategoryDoesNotExistException(command, command.getCategoryId());
    }

    menuHelperService.removeCategory(menu, command.getCategoryId());
    menuRepository.save(menu);
  }

  public void update(Menu menu, UpdateCategoryCommand command) {

    // check by Id
    Category category =
        menuHelperService.checkCategoryExistsById(command, menu, command.getCategoryId());

    // Check By name
    menuHelperService.verifyCategoryNameNotAlreadyExisting(
        command, menu, command.getCategoryId(), command.getName());

    Category updatedCategory = updateCategoryCommandMapper.fromDto(command);
    updatedCategory.setItems(category.getItems());

    menuHelperService.addOrUpdateCategory(menu, updatedCategory);
    menuRepository.save(menu);
  }
}
