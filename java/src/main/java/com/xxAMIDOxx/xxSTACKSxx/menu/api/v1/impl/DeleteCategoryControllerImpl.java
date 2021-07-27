package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.DeleteCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.DeleteCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.DeleteCategoryHandler;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** @author ArathyKrishna */
@RestController
public class DeleteCategoryControllerImpl implements DeleteCategoryController {

  private DeleteCategoryHandler handler;

  public DeleteCategoryControllerImpl(DeleteCategoryHandler handler) {
    this.handler = handler;
  }

  @Override
  public ResponseEntity<Void> deleteCategory(UUID menuId, UUID categoryId, String correlationId) {

    handler.handle(new DeleteCategoryCommand(correlationId, menuId, categoryId));
    return new ResponseEntity<>(OK);
  }
}
