package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;
import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.UpdateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.UpdateCategoryHandler;
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
