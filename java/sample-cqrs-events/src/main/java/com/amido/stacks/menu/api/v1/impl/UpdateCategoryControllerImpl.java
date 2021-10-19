package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.mappers.RequestToCommandMapper.map;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.menu.api.v1.UpdateCategoryController;
import com.amido.stacks.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.menu.handlers.UpdateCategoryHandler;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
