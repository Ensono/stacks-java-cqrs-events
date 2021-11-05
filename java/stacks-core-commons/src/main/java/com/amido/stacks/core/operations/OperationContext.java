package com.amido.stacks.core.operations;

public abstract class OperationContext {

  //  private int operationCode;
  private String correlationId;

  public OperationContext(String correlationId) {
    //    this.operationCode = operationCode;
    this.correlationId = correlationId;
  }

  public abstract int getOperationCode();

  /** No arg constructor. */
  public OperationContext() {}

  //  public int getOperationCode() {
  //    return operationCode;
  //  }

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
        + getOperationCode()
        + ", correlationId="
        + correlationId
        + '}';
  }
}
