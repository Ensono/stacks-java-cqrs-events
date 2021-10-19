package com.amido.stacks.menu.api.exception;

import com.amido.stacks.menu.commands.OperationCode;
import com.amido.stacks.menu.exception.ExceptionCode;

public class ApiException extends RuntimeException {

  ExceptionCode exceptionCode;
  OperationCode operationCode;
  String correlationId;

  public ApiException(
      String message,
      ExceptionCode exceptionCode,
      OperationCode operationCode,
      String correlationId) {
    super(message);
    this.exceptionCode = exceptionCode;
    this.operationCode = operationCode;
    this.correlationId = correlationId;
  }

  public ExceptionCode getExceptionCode() {
    return exceptionCode;
  }

  public OperationCode getOperationCode() {
    return operationCode;
  }

  public String getCorrelationId() {
    return correlationId;
  }
}
