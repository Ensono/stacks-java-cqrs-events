package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(
    path = "/v1/menu/{id}/category",
    consumes = "application/json",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.POST)
public interface CreateCategoryController {

  @PostMapping
  @Operation(
      tags = "Category",
      summary = "Create a category in the menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Adds a category to menu",
      operationId = "AddMenuCategory")
  @CreateAPIResponses
  ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Valid @RequestBody CreateCategoryRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
