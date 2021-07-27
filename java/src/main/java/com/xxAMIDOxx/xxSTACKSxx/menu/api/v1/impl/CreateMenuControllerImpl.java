package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.CreateMenuHandler;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateMenuControllerImpl implements CreateMenuController {

  private CreateMenuHandler createMenuHandler;

  public CreateMenuControllerImpl(CreateMenuHandler createMenuHandler) {
    this.createMenuHandler = createMenuHandler;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> createMenu(
      @Valid CreateMenuRequest body, String correlationId) {
    return new ResponseEntity<>(
        new ResourceCreatedResponse(createMenuHandler.handle(map(correlationId, body)).get()),
        HttpStatus.CREATED);
  }
}
