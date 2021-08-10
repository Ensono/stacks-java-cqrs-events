package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.mappers.RequestToCommandMapper.map;

import com.amido.stacks.menu.api.v1.CreateItemController;
import com.amido.stacks.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.amido.stacks.menu.handlers.CreateItemHandler;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateItemControllerImpl implements CreateItemController {

  private CreateItemHandler createItemHandler;

  public CreateItemControllerImpl(CreateItemHandler createItemHandler) {
    this.createItemHandler = createItemHandler;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuItem(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {
    return new ResponseEntity<>(
        new ResourceCreatedResponse(
            createItemHandler.handle(map(correlationId, menuId, categoryId, body)).get()),
        HttpStatus.CREATED);
  }
}
