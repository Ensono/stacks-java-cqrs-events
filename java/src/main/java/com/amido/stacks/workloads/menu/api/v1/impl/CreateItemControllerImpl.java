package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.CreateItemController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.workloads.menu.handlers.CreateItemHandler;
import com.amido.stacks.workloads.menu.mappers.RequestToCommandMapper;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateItemControllerImpl implements CreateItemController {

  private CreateItemHandler createItemHandler;

  @Autowired private RequestToCommandMapper requestToCommandMapper;

  public CreateItemControllerImpl(CreateItemHandler createItemHandler) {
    this.createItemHandler = createItemHandler;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuItem(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {
    return new ResponseEntity<>(
        new ResourceCreatedResponse(
            createItemHandler
                .handle(requestToCommandMapper.map(correlationId, menuId, categoryId, body))
                .orElseThrow()),
        HttpStatus.CREATED);
  }
}
