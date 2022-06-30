package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(
    path = "/v1/menu",
    consumes = "application/json",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.POST)
public interface CreateMenuController {

  @PostMapping
  @Operation(
      tags = "Menu",
      summary = "Create a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Adds a menu",
      operationId = "CreateMenu")
  @CreateAPIResponses
  ResponseEntity<ResourceCreatedResponse> createMenu(
      @Valid @RequestBody CreateMenuRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
