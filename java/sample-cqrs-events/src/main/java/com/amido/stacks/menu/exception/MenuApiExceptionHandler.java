package com.amido.stacks.menu.exception;

import com.amido.stacks.core.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class MenuApiExceptionHandler {

  @ResponseBody
  @ExceptionHandler(MenuAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorResponse menuAlreadyExistsExceptionHandler(MenuAlreadyExistsException ex) {
    return new ErrorResponse(
        ex.getExceptionCode(),
        ex.getOperationCode(),
        ex.getCorrelationId(),
        ex.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(MenuNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse menuNotFoundExceptionHandler(MenuNotFoundException ex) {
    return new ErrorResponse(
        ex.getExceptionCode(),
        ex.getOperationCode(),
        ex.getCorrelationId(),
        ex.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(CategoryAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorResponse categoryAlreadyExistsExceptionHandler(CategoryAlreadyExistsException ex) {
    return new ErrorResponse(
        ex.getExceptionCode(),
        ex.getOperationCode(),
        ex.getCorrelationId(),
        ex.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(CategoryDoesNotExistException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse categoryNotFoundExceptionHandler(CategoryDoesNotExistException ex) {
    return new ErrorResponse(
        ex.getExceptionCode(),
        ex.getOperationCode(),
        ex.getCorrelationId(),
        ex.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(ItemAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorResponse itemAlreadyExistsExceptionHandler(ItemAlreadyExistsException ex) {
    return new ErrorResponse(
        ex.getExceptionCode(),
        ex.getOperationCode(),
        ex.getCorrelationId(),
        ex.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(ItemDoesNotExistsException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse itemDontNotExistsExceptionHandler(ItemDoesNotExistsException ex) {
    return new ErrorResponse(
        ex.getExceptionCode(),
        ex.getOperationCode(),
        ex.getCorrelationId(),
        ex.getMessage());
  }
}
