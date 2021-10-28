package com.amido.stacks.menu.api.exception;

public abstract class ApiException extends RuntimeException {

//  String exceptionCode;
  int operationCode;
  String correlationId;
//
//  public ApiException(
//      String message,
//      ExceptionCode exceptionCode,
//      OperationCode operationCode,
//      String correlationId) {
//    super(message);
//    this.exceptionCode = exceptionCode;
//    this.operationCode = operationCode;
//    this.correlationId = correlationId;
//  }
  public ApiException(
      String message,
      int operationCode,
      String correlationId) {
    super(message);
    this.operationCode = operationCode;
    this.correlationId = correlationId;
  }

//  public ExceptionCode getExceptionCode() {
//    return exceptionCode;
//  }

  public int getOperationCode() {
    return operationCode;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public abstract int getExceptionCode();
}
