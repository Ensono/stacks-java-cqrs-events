package com.amido.stacks.api.templates;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import net.serenitybdd.rest.SerenityRest;

public class TemplateResponse {
  public Map<String, String> returned() {
    return mapOfStringsFrom(SerenityRest.lastResponse().getBody().as(Map.class));
  }

  private Map<String, String> mapOfStringsFrom(Map<String, Object> map) {
    return map.entrySet().stream()
        .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));
  }
}
