package com.xxAMIDOxx.xxSTACKSxx.core.operations;

public abstract class OperationContext {

  private int operationCode;
  private String correlationId;

  public OperationContext(int operationCode, String correlationId) {
    this.operationCode = operationCode;
    this.correlationId = correlationId;
  }

  /** No arg constructor. */
  public OperationContext() {}

  public int getOperationCode() {
    return operationCode;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  @Override
  public String toString() {
    return "OperationContext{"
        + "operationCode="
        + operationCode
        + ", correlationId="
        + correlationId
        + '}';
  }
}
