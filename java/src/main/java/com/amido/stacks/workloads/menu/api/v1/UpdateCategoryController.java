package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ArathyKrishna
 */
@RequestMapping(
    path = "/v1/menu/{id}/category/{categoryId}",
    consumes = "application/json",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.PUT)
public interface UpdateCategoryController {

  @PutMapping
  @Operation(
      tags = "Category",
      summary = "Update a category in the menu",
      description = "Update a category to menu",
      operationId = "UpdateMenuCategory")
  @UpdateAPIResponses
  ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Valid @RequestBody UpdateCategoryRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
