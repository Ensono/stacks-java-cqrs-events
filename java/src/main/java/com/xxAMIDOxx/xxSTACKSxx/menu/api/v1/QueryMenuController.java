package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1;

import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.SearchMenuResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/v1/menu")
public interface QueryMenuController {

  @GetMapping(produces = "application/json; charset=utf-8")
  @Operation(
      tags = "Menu",
      summary = "Get or search a list of menus",
      security = @SecurityRequirement(name = "bearerAuth"),
      description =
          "By passing in the appropriate options, you can search for available menus in the system",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Search results matching criteria",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SearchMenuResult.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  ResponseEntity<SearchMenuResult> searchMenu(
      @RequestParam(value = "searchTerm", required = false) String searchTerm,
      @RequestParam(value = "restaurantId", required = false) UUID restaurantId,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber)
      throws IOException;

  @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
  @Operation(
      tags = "Menu",
      summary = "Get a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description =
          "By passing the menu id, you can get access to available categories and items in the menu",
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
