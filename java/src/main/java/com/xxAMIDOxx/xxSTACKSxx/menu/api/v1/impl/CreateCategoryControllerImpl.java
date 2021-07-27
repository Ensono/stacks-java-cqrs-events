package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.CreateCategoryHandler;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  private CreateCategoryHandler createCategoryHandler;

  public CreateCategoryControllerImpl(CreateCategoryHandler createCategoryHandler) {
    this.createCategoryHandler = createCategoryHandler;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    return new ResponseEntity<>(
        new ResourceCreatedResponse(
            createCategoryHandler.handle(map(correlationId, menuId, body)).get()),
        HttpStatus.CREATED);
  }
}
