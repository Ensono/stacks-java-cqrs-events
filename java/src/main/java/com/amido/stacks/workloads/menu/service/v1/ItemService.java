package com.amido.stacks.workloads.menu.service.v1;

import com.amido.stacks.workloads.menu.commands.CreateItemCommand;
import com.amido.stacks.workloads.menu.commands.DeleteItemCommand;
import com.amido.stacks.workloads.menu.commands.UpdateItemCommand;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.cqrs.CreateItemCommandMapper;
import com.amido.stacks.workloads.menu.mappers.cqrs.UpdateItemCommandMapper;
#if DYNAMODB
import com.amido.stacks.workloads.menu.repository.StacksDynamoDbRepository;

#elif COSMOSDB
import com.amido.stacks.cosmosdb.repository.StacksCosmosRepository;
#endif
import com.amido.stacks.workloads.menu.service.v1.utility.MenuHelperService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  #if DYNAMODB
  private final StacksDynamoDbRepository menuRepository;

  #elif COSMOSDB
  private final StacksCosmosRepository menuRepository;
  #endif  private final MenuHelperService menuHelperService;
  private final CreateItemCommandMapper createItemCommandMapper;
  private final UpdateItemCommandMapper updateItemCommandMapper;

  public Optional<Menu> create(Menu menu, CreateItemCommand createItemCommand) {

    Category category =
        menuHelperService.checkCategoryExistsById(
            createItemCommand, menu, createItemCommand.getCategoryId());

    UUID itemId = UUID.randomUUID();

    menuHelperService.verifyItemNameNotAlreadyExisting(
        createItemCommand, category, itemId, createItemCommand.getName());

    Item item = createItemCommandMapper.fromDto(createItemCommand);
    item.setId(itemId.toString());

    createItemCommand.setItemId(itemId);

    menuHelperService.addOrUpdateItem(category, item);
    menuHelperService.addOrUpdateCategory(menu, category);

    menuRepository.save(menu);

    return Optional.of(menu);
  }

  public void delete(Menu menu, DeleteItemCommand command) {

    Category category =
        menuHelperService.checkCategoryExistsById(command, menu, command.getCategoryId());

    menuHelperService.checkItemExistsById(command, category, command.getItemId());

    menuHelperService.removeItem(category, command.getItemId());
    menuHelperService.addOrUpdateCategory(menu, category);

    menuRepository.save(menu);
  }

  public void update(Menu menu, UpdateItemCommand command) {

    Category category =
        menuHelperService.checkCategoryExistsById(command, menu, command.getCategoryId());

    menuHelperService.checkItemExistsById(command, category, command.getItemId());

    menuHelperService.verifyItemNameNotAlreadyExisting(
        command, category, command.getItemId(), command.getName());

    Item updated = updateItemCommandMapper.fromDto(command);
    menuHelperService.addOrUpdateItem(category, updated);

    menuRepository.save(menu);
  }
}
