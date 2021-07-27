package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author ArathyKrishna */
public class CategoryHelper {
  public static List<Category> createCategories(int count) {
    List<Category> categoryList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      categoryList.add(createCategory(i));
    }
    return categoryList;
  }

  public static Category createCategory(int counter) {
    return new Category(
        UUID.randomUUID().toString(),
        counter + " Category",
        counter + " Menu Description",
        new ArrayList<Item>());
  }
}
