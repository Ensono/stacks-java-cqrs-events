package com.amido.stacks.menu.exception;

import com.amido.stacks.core.api.exception.ApiException;
import com.amido.stacks.menu.commands.MenuCommand;
import com.amido.stacks.menu.commands.OperationCode;

public class MenuApiException extends ApiException {

  public MenuApiException(String message, ExceptionCode exceptionCode, MenuCommand menuCommand) {
    super(
        message,
        exceptionCode,
        OperationCode.fromCode(menuCommand.getOperationCode()),
        menuCommand.getCorrelationId());
  }
}
