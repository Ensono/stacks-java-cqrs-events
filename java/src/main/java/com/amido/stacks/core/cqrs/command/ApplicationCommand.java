package com.amido.stacks.core.cqrs.command;

import com.amido.stacks.core.operations.OperationContext;

public abstract class ApplicationCommand extends OperationContext {
  public ApplicationCommand(int operationCode, String correlationId) {
    super(operationCode, correlationId);
  }
}
