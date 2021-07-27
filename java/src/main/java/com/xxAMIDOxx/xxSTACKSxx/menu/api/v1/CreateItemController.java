package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1;

import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/menu/{id}/category/{categoryId}/items")
public interface CreateItemController {

  @PostMapping(consumes = "application/json", produces = "application/json; charset=utf-8")
  @Operation(
      tags = "Item",
      summary = "Add an item to an existing category in a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Adds a menu item",
      operationId = "AddMenuItem",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Resource created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResourceCreatedResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized, Access token is missing or invalid",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden, the user does not have permission to execute this operation",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict, an item already exists",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  ResponseEntity<ResourceCreatedResponse> addMenuItem(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Valid @RequestBody CreateItemRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
