package com.xxAMIDOxx.xxSTACKSxx.api;

public enum ExceptionMessages {
  MENU_ALREADY_EXISTS(
      "A Menu with the name '(.*)' already exists for the restaurant with id '(.*)'."),
  MENU_DOES_NOT_EXIST("A menu with id '(.*)' does not exist."),

  CATEGORY_DOES_NOT_EXIST("A category with the id '(.*)' does not exist for menu with id '(.*)'."),
  CATEGORY_ALREADY_EXISTS(
      "A category with the name '(.*)' already exists for the menu with id '(.*)'."),

  ITEM_ALREADY_EXISTS(
      "An item with the name '(.*)' already exists for the category '(.*)' in menu with id '(.*)'."),
  ITEM_DOES_NOT_EXIST(
      "An item with the id '(.*)' does not exists for category with the id '(.*)' and for menu with id '(.*)'.");

  private final String message;

  ExceptionMessages(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
