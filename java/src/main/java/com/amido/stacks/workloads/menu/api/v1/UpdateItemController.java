package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ArathyKrishna
 */
@RequestMapping(
    path = "/v1/menu/{id}/category/{categoryId}/items/{itemId}",
    consumes = "application/json",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.PUT)
public interface UpdateItemController {
  @PutMapping
  @Operation(
      tags = "Item",
      summary = "Update an item in the menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Update an item in the menu",
      operationId = "UpdateMenuItem")
  @UpdateAPIResponses
  ResponseEntity<ResourceUpdatedResponse> updateItem(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Parameter(description = "Item id", required = true) @PathVariable("itemId") UUID itemId,
      @Valid @RequestBody UpdateItemRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
