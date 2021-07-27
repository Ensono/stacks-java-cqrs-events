package com.xxAMIDOxx.xxSTACKSxx.core.api.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ExceptionCode;

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
