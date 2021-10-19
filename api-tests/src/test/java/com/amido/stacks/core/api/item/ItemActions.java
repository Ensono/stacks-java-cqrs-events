package com.amido.stacks.core.api.item;

import com.amido.stacks.core.api.models.Item;
import java.util.Map;

public class ItemActions {
  public static Item mapToItem(Map<String, String> properties, String itemId) {
    return new Item(
        itemId,
        properties.get("name"),
        properties.get("description"),
        Double.valueOf(properties.get("price")),
        Boolean.parseBoolean(properties.get("available")));
  }
}
