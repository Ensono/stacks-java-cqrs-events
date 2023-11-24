package com.amido.stacks.workloads.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuRequest {

  @JsonProperty("name")
  @NotBlank
  private String name = null;

  @JsonProperty("description")
  @NotBlank
  private String description = null;

  @JsonProperty("tenantId")
  @NotNull
  private UUID tenantId = null;

  @JsonProperty("enabled")
  @NotNull
  private Boolean enabled = null;
}
