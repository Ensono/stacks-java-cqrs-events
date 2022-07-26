package com.amido.stacks.workloads.menu.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

  private String id;

  private String name;

  private String description;

  @Builder.Default private List<Item> items = new ArrayList<>();
}
