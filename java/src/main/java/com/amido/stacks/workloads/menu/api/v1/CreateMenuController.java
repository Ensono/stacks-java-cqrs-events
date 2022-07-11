package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.annotations.CreateAPIResponses;
import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(
    path = "/v1/menu",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8",
    method = RequestMethod.POST)
public interface CreateMenuController {

  @PostMapping
  @Operation(
      tags = "Menu",
      summary = "Create a menu",
      description = "Adds a menu",
      operationId = "CreateMenu")
  @CreateAPIResponses
  ResponseEntity<ResourceCreatedResponse> createMenu(
      @Valid @RequestBody CreateMenuRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
