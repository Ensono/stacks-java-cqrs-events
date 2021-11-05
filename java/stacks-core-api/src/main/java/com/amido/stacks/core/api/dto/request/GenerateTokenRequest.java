package com.amido.stacks.core.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTokenRequest {

  @JsonProperty("client_id")
  @NotBlank
  private String client_id = null;

  @JsonProperty("client_secret")
  @NotBlank
  private String client_secret = null;

  @JsonProperty("audience")
  @NotNull
  private String audience = null;

  @JsonProperty("grant_type")
  @NotNull
  private String grant_type = null;
}
