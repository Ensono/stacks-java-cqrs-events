package com.amido.stacks.workloads.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {

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
