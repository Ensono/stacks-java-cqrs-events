package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(
    path = "/v1/menu/{id}/category/{categoryId}/items",
    consumes = "application/json",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.POST)
public interface CreateItemController {

  @PostMapping
  @Operation(
      tags = "Item",
      summary = "Add an item to an existing category in a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Adds a menu item",
      operationId = "AddMenuItem")
  @CreateAPIResponses
  ResponseEntity<ResourceCreatedResponse> addMenuItem(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Valid @RequestBody CreateItemRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
