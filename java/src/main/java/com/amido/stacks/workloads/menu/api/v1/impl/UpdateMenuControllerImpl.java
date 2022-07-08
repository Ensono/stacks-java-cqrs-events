package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.UpdateMenuController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.commands.UpdateMenuCommand;
import com.amido.stacks.workloads.menu.handlers.UpdateMenuHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static com.amido.stacks.workloads.menu.mappers.RequestToCommandMapper.map;

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
