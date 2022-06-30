package com.amido.stacks.workloads.menu.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author ArathyKrishna
 */
@RequestMapping(
    path = "/v1/menu/{id}/category/{categoryId}/items/{itemId}",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.DELETE)
public interface DeleteItemController {

  @DeleteMapping
  @Operation(
      tags = "Item",
      summary = "Removes an item from menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Removes an item from menu",
      operationId = "DeleteMenuItem")
  @DeleteAPIResponses
  ResponseEntity<Void> deleteItem(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Parameter(description = "Item id", required = true) @PathVariable("itemId") UUID itemId,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
