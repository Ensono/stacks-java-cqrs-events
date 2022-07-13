package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.annotations.DeleteAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(
    path = "/v1/menu/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8",
    method = RequestMethod.DELETE)
public interface DeleteMenuController {

  @DeleteMapping
  @Operation(
      tags = "Menu",
      summary = "Removes a Menu with all it's Categories and Items",
      description = "Remove a menu from a restaurant",
      operationId = "DeleteMenu")
  @DeleteAPIResponses
  ResponseEntity<Void> deleteMenu(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
