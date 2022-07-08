package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.annotations.DeleteAPIResponses;
import com.amido.stacks.core.api.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(
    path = "/v1/menu/{id}/category/{categoryId}/items/{itemId}",
    produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8",
    method = RequestMethod.DELETE)
public interface DeleteItemController {

  @DeleteMapping
  @Operation(
      tags = "Item",
      summary = "Removes an item from menu",
      description = "Removes an item from menu",
      operationId = "DeleteMenuItem")
  @ApiResponse(
      responseCode = "404",
      description = "Resource not found",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  @DeleteAPIResponses
  ResponseEntity<Void> deleteItem(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Parameter(description = "Item id", required = true) @PathVariable("itemId") UUID itemId,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
