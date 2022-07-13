package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.CreateCategoryController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.handlers.CreateCategoryHandler;
import com.amido.stacks.workloads.menu.mappers.RequestToCommandMapper;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  private CreateCategoryHandler createCategoryHandler;

  @Autowired private RequestToCommandMapper requestToCommandMapper;

  public CreateCategoryControllerImpl(CreateCategoryHandler createCategoryHandler) {
    this.createCategoryHandler = createCategoryHandler;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    return new ResponseEntity<>(
        new ResourceCreatedResponse(
            createCategoryHandler
                .handle(requestToCommandMapper.map(correlationId, menuId, body))
                .orElseThrow()),
        HttpStatus.CREATED);
  }
}
