package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.UpdateMenuController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.commands.UpdateMenuCommand;
import com.amido.stacks.workloads.menu.handlers.UpdateMenuHandler;
import com.amido.stacks.workloads.menu.mappers.RequestToCommandMapper;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateMenuControllerImpl implements UpdateMenuController {

  private UpdateMenuHandler updateMenuHandler;

  @Autowired private RequestToCommandMapper requestToCommandMapper;

  public UpdateMenuControllerImpl(UpdateMenuHandler updateMenuHandler) {
    this.updateMenuHandler = updateMenuHandler;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenu(
      UUID menuId, @Valid UpdateMenuRequest body, String correlationId) {
    UpdateMenuCommand command = requestToCommandMapper.map(correlationId, menuId, body);
    return new ResponseEntity<>(
        new ResourceUpdatedResponse(updateMenuHandler.handle(command).orElseThrow()),
        HttpStatus.OK);
  }
}
