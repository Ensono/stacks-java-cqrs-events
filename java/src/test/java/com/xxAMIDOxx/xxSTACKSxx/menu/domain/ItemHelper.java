package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author ArathyKrishna */
public class ItemHelper {

  public static Item createItem(int counter) {
    return new Item(
        UUID.randomUUID().toString(), counter + " New item", "New item Description", 12.1d, true);
  }

  public static List<Item> createItems(int count) {
    List<Item> itemList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      itemList.add(createItem(i));
    }
    return itemList;
  }
}
