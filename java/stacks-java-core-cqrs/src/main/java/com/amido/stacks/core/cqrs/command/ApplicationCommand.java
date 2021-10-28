package com.amido.stacks.core.cqrs.command;

import com.amido.stacks.core.operations.OperationContext;

public abstract class ApplicationCommand extends OperationContext {
  public ApplicationCommand(String correlationId) {
    super(correlationId);
  }
}
