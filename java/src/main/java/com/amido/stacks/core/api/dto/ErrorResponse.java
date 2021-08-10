package com.amido.stacks.core.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  int errorCode;
  int operationCode;
  String correlationId;
  String description;
}
