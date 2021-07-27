package com.xxAMIDOxx.xxSTACKSxx.menu.api.v2;

import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v2/menu")
public interface QueryMenuControllerV2 {

  @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
  @Operation(
      tags = "Menu",
      summary = "Get a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description =
          "By passing the menu id, you can get access to available categories and items in the menu",
      operationId = "GetMenuV2",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MenuDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Menu Not Found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  ResponseEntity<MenuDTO> getMenu(
      @PathVariable(name = "id") UUID id,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
