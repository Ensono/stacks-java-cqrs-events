package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

  private String id;

  private String name;

  private String description;

  private Double price;

  private Boolean available;
}
