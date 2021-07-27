package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.UpdateMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.UpdateMenuHandler;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateMenuControllerImpl implements UpdateMenuController {

  private UpdateMenuHandler updateMenuHandler;

  public UpdateMenuControllerImpl(UpdateMenuHandler updateMenuHandler) {
    this.updateMenuHandler = updateMenuHandler;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenu(
      UUID menuId, @Valid UpdateMenuRequest body, String correlationId) {
    UpdateMenuCommand command = map(correlationId, menuId, body);
    return new ResponseEntity<>(
        new ResourceUpdatedResponse(updateMenuHandler.handle(command).get()), HttpStatus.OK);
  }
}
