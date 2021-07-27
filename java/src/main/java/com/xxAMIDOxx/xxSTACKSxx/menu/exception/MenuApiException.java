package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.core.api.exception.ApiException;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;

public class MenuApiException extends ApiException {

  public MenuApiException(String message, ExceptionCode exceptionCode, MenuCommand menuCommand) {
    super(
        message,
        exceptionCode,
        OperationCode.fromCode(menuCommand.getOperationCode()),
        menuCommand.getCorrelationId());
  }
}
