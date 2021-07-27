package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1;

import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/** @author ArathyKrishna */
@RequestMapping("/v1/menu/{id}/category/{categoryId}/items/{itemId}")
public interface DeleteItemController {

  @DeleteMapping(produces = "application/json; charset=utf-8")
  @Operation(
      tags = "Item",
      summary = "Removes an item from menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description = "Removes an item from menu",
      operationId = "DeleteMenuItem",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
        @ApiResponse(
            responseCode = "204",
            description = "No Content",
            content = @Content(schema = @Schema(hidden = true))),
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
            responseCode = "404",
            description = "Resource not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  ResponseEntity<Void> deleteItem(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(description = "Category id", required = true) @PathVariable("categoryId")
          UUID categoryId,
      @Parameter(description = "Item id", required = true) @PathVariable("itemId") UUID itemId,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
