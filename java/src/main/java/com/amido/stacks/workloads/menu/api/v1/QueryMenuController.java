package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RequestMapping(
    path = "/v1/menu",
    produces = "application/json; charset=utf-8",
    method = RequestMethod.GET)
public interface QueryMenuController {

  @GetMapping
  @Operation(
      tags = "Menu",
      summary = "Get or search a list of menus",
      security = @SecurityRequirement(name = "bearerAuth"),
      description =
          "By passing in the appropriate options, you can search for available menus in the system")
  @SearchAPIResponses
  ResponseEntity<SearchMenuResult> searchMenu(
      @RequestParam(value = "searchTerm", required = false) String searchTerm,
      @RequestParam(value = "restaurantId", required = false) UUID restaurantId,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber)
      throws IOException;

  @GetMapping(value = "/{id}")
  @Operation(
      tags = "Menu",
      summary = "Get a menu",
      security = @SecurityRequirement(name = "bearerAuth"),
      description =
          "By passing the menu id, you can get access to available categories and items in the menu")
  @ReadAPIResponses
  ResponseEntity<MenuDTO> getMenu(
      @PathVariable(name = "id") UUID id,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
