package com.xxAMIDOxx.xxSTACKSxx.menu.events;

public enum EventCode {

  // MenuDTO operations
  MENU_CREATED(101),
  MENU_UPDATED(102),
  MENU_DELETED(103),

  // Categories Operations
  CATEGORY_CREATED(201),
  CATEGORY_UPDATED(202),
  CATEGORY_DELETED(203),

  // Items Operations
  MENU_ITEM_CREATED(301),
  MENU_ITEM_UPDATED(302),
  MENU_ITEM_DELETED(303);

  private final int code;

  EventCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
