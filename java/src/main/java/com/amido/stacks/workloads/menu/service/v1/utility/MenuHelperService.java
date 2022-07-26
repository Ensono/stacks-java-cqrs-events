package com.amido.stacks.workloads.menu.service.v1.utility;

import com.amido.stacks.workloads.menu.commands.MenuCommand;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.CategoryAlreadyExistsException;
import com.amido.stacks.workloads.menu.exception.CategoryDoesNotExistException;
import com.amido.stacks.workloads.menu.exception.ItemAlreadyExistsException;
import com.amido.stacks.workloads.menu.exception.ItemDoesNotExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuHelperService {

  public void addOrUpdateCategory(Menu menu, Category category) {

    if (menu.getCategories() == null) {
      menu.setCategories(new ArrayList<>());
    }

    List<Category> newCategories =
        menu.getCategories().stream()
            .filter(c -> !c.getId().equals(category.getId()))
            .collect(Collectors.toList());

    newCategories.add(category);

    menu.setCategories(newCategories);
  }

  public void addOrUpdateItem(Category category, Item item) {

    if (category.getItems() == null) {
      category.setItems(new ArrayList<>());
    }

    List<Item> newItems =
        category.getItems().stream()
            .filter(c -> !c.getId().equals(item.getId()))
            .collect(Collectors.toList());

    newItems.add(item);

    category.setItems(newItems);
  }

  public Category checkCategoryExistsById(MenuCommand command, Menu menu, UUID categoryId) {

    if (menu.getCategories() == null) {
      menu.setCategories(new ArrayList<>());
    }

    Optional<Category> optCategory =
        menu.getCategories().stream()
            .filter(c -> categoryId.toString().equals(c.getId()))
            .findFirst();

    if (optCategory.isEmpty()) {
      throw new CategoryDoesNotExistException(command, categoryId);
    }

    return optCategory.get();
  }

  public Item checkItemExistsById(MenuCommand command, Category category, UUID itemId) {

    if (category.getItems() == null) {
      category.setItems(new ArrayList<>());
    }

    Optional<Item> optItem =
        category.getItems().stream().filter(i -> itemId.toString().equals(i.getId())).findFirst();

    if (optItem.isEmpty()) {
      throw new ItemDoesNotExistsException(command, UUID.fromString(category.getId()), itemId);
    }

    return optItem.get();
  }

  public void verifyCategoryNameNotAlreadyExisting(
      MenuCommand command, Menu menu, UUID categoryId, String name) {

    if (menu.getCategories() == null) {
      menu.setCategories(new ArrayList<>());
    }

    Optional<Category> optCategory =
        menu.getCategories().stream().filter(c -> c.getName().equals(name)).findFirst();

    if (optCategory.isPresent() && isCategoryIdNullOrMatchesId(categoryId, optCategory.get())) {

      throw new CategoryAlreadyExistsException(command, name);
    }
  }

  public boolean isCategoryIdNullOrMatchesId(UUID categoryId, Category category) {
    return (categoryId == null || !category.getId().equals(categoryId.toString()));
  }

  public void verifyItemNameNotAlreadyExisting(
      MenuCommand command, Category category, UUID itemId, String name) {

    if (category.getItems() == null) {
      category.setItems(new ArrayList<>());
    }

    Optional<Item> optItem =
        category.getItems().stream().filter(c -> c.getName().equals(name)).findFirst();

    if (optItem.isPresent() && !optItem.get().getId().equals(itemId.toString())) {
      throw new ItemAlreadyExistsException(command, UUID.fromString(category.getId()), name);
    }
  }

  public void removeItem(Category category, UUID itemId) {
    category.getItems().removeIf(i -> i.getId().equals(itemId.toString()));
  }

  public void removeCategory(Menu menu, UUID categoryId) {
    menu.getCategories().removeIf(c -> c.getId().equals(categoryId.toString()));
  }
}
