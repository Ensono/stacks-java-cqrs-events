package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.UpdateCategoryController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.handlers.UpdateCategoryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static com.amido.stacks.workloads.menu.mappers.RequestToCommandMapper.map;
import static org.springframework.http.HttpStatus.OK;

/**
 * Controller for updating category.
 *
 * @author ArathyKrishna
 */
@RestController
public class UpdateCategoryControllerImpl implements UpdateCategoryController {

  private UpdateCategoryHandler handler;

  public UpdateCategoryControllerImpl(UpdateCategoryHandler handler) {
    this.handler = handler;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {
    return new ResponseEntity<>(
        new ResourceUpdatedResponse(
            handler.handle(map(correlationId, menuId, categoryId, body)).get()),
        OK);
  }
}
