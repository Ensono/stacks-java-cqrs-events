package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(
    path = "/v1/menu/{id}",
    consumes = "application/json",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.PUT)
public interface UpdateMenuController {

  @PutMapping(consumes = "application/json", produces = "application/json; charset=utf-8")
  @Operation(
      tags = "Menu",
      summary = "Update a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Update a menu with new information")
  @UpdateAPIResponses
  ResponseEntity<ResourceUpdatedResponse> updateMenu(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Valid @RequestBody UpdateMenuRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
