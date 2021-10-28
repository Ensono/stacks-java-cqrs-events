package com.amido.stacks.menu.exception;

import com.amido.stacks.menu.api.exception.ApiException;
import com.amido.stacks.menu.commands.MenuCommand;

public class MenuApiException extends ApiException {

  private static final int EXCEPTION_CODE = 10000;

  public MenuApiException(String message, MenuCommand menuCommand) {
    super(
        message,
        menuCommand.getOperationCode(),
        menuCommand.getCorrelationId());
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
