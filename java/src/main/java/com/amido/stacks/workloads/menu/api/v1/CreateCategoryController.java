package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.annotations.CreateAPIResponses;
import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(
    path = "/v1/menu/{id}/category",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8",
    method = RequestMethod.POST)
public interface CreateCategoryController {

  @PostMapping
  @Operation(
      tags = "Category",
      summary = "Create a category in the menu",
      description = "Adds a category to menu",
      operationId = "AddMenuCategory")
  @CreateAPIResponses
  ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Valid @RequestBody CreateCategoryRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
