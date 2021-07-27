package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

public enum OperationCode {
  // MenuDTO operations
  CREATE_MENU(101),
  UPDATE_MENU(102),
  DELETE_MENU(103),
  GET_MENU_BY_ID(104),
  SEARCH_MENU(110),

  // Categories Operations
  CREATE_CATEGORY(201),
  UPDATE_CATEGORY(202),
  DELETE_CATEGORY(203),

  // Items Operations
  CREATE_MENU_ITEM(301),
  UPDATE_MENU_ITEM(302),
  DELETE_MENU_ITEM(303);

  private final int code;

  OperationCode(int code) {
    this.code = code;
  }

  public static OperationCode fromCode(int code) {
    for (OperationCode e : values()) {
      if (e.code == code) {
        return e;
      }
    }
    return null;
  }

  public int getCode() {
    return code;
  }
}
