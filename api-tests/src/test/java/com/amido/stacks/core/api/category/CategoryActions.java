package com.amido.stacks.core.api.category;

import com.amido.stacks.core.api.models.Category;
import java.util.ArrayList;
import java.util.Map;

public class CategoryActions {

  public static Category mapToCategory(Map<String, String> properties, String id) {
    return new Category(
        id, properties.get("name"), properties.get("description"), new ArrayList<>());
  }
}
