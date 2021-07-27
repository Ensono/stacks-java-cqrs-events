package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author ArathyKrishna */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequest {

  @JsonProperty("name")
  @NotBlank
  private String name = null;

  @JsonProperty("description")
  @NotBlank
  private String description = null;

  @JsonProperty("price")
  @NotNull
  @Positive(message = "Price must be greater than zero")
  private Double price = null;

  @JsonProperty("available")
  @NotNull
  private Boolean available = null;
}
