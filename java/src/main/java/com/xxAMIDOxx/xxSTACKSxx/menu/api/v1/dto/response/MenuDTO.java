package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Menu")
public class MenuDTO {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("restaurantId")
  private UUID restaurantId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("categories")
  @Builder.Default
  private List<CategoryDTO> categories = new ArrayList<>();

  @JsonProperty("enabled")
  private Boolean enabled;
}
