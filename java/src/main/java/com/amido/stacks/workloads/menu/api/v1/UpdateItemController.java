package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.annotations.UpdateAPIResponses;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(
    path = "/v1/menu/{id}/category/{categoryId}/items/{itemId}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8",
    method = RequestMethod.PUT)
public interface UpdateItemController {
  @PutMapping
  @Operation(
      tags = "Item",
      summary = "Update an item in the menu",
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
