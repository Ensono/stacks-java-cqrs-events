package com.amido.stacks.workloads.menu.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for Deleting a menu
 *
 * @author ArathyKrishna
 */
@RequestMapping(
    path = "/v1/menu/{id}",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.DELETE)
public interface DeleteMenuController {

  @DeleteMapping
  @Operation(
      tags = "Menu",
      summary = "Removes a Menu with all it's Categories and Items",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Remove a menu from a restaurant",
      operationId = "DeleteMenu")
  @DeleteAPIResponses
  ResponseEntity<Void> deleteMenu(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
