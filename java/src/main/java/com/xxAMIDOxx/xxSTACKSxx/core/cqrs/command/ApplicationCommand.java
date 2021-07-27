package com.xxAMIDOxx.xxSTACKSxx.core.cqrs.command;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

public abstract class ApplicationCommand extends OperationContext {
  public ApplicationCommand(int operationCode, String correlationId) {
    super(operationCode, correlationId);
  }
}
