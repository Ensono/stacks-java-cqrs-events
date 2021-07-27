package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author ArathyKrishna */
public class MenuHelper {

  public static List<Menu> createMenus(int count) {
    List<Menu> menuList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      menuList.add(createMenu(i));
    }
    return menuList;
  }

  public static Menu createMenu(int counter) {
    return new Menu(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        counter + " Menu",
        counter + " Menu Description",
        new ArrayList<Category>(),
        true);
  }
}
