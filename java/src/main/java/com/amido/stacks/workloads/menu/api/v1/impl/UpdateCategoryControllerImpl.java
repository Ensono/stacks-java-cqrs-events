package com.amido.stacks.workloads.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.workloads.menu.api.v1.UpdateCategoryController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.handlers.UpdateCategoryHandler;
import com.amido.stacks.workloads.menu.mappers.RequestToCommandMapper;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateCategoryControllerImpl implements UpdateCategoryController {

  private UpdateCategoryHandler handler;

  @Autowired private RequestToCommandMapper requestToCommandMapper;

  public UpdateCategoryControllerImpl(UpdateCategoryHandler handler) {
    this.handler = handler;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {
    return new ResponseEntity<>(
        new ResourceUpdatedResponse(
            handler
                .handle(requestToCommandMapper.map(correlationId, menuId, categoryId, body))
                .orElseThrow()),
        OK);
  }
}
