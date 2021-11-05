package com.amido.stacks.core.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTokenResponse {

  @JsonProperty("access_token")
  private String access_token = null;

  @JsonProperty("expires_in")
  private String expires_in = null;

  @JsonProperty("token_type")
  private String token_type = null;
}
