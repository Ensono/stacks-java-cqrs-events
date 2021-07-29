package com.amido.stacks.api;

public enum WebServiceEndPoints {
  BASE_URL(System.getenv("BASE_URL")),
  CATEGORY("/category"),
  STATUS("/health"),
  MENU("/v1/menu"),
  MENU_V2("/v2/menu"),
  ITEMS("/items");

  private final String url;

  WebServiceEndPoints(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
