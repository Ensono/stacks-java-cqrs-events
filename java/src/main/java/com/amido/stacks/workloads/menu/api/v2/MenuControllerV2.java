package com.amido.stacks.workloads.menu.api.v2;

import com.amido.stacks.core.api.annotations.ReadAPIResponses;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.commands.MenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.service.MenuQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v2/menu", produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
@RestController
@RequiredArgsConstructor
public class MenuControllerV2 {

  private final MenuMapper menuMapper;

  private final MenuQueryService menuQueryService;

  @GetMapping(value = "/{id}")
  @Operation(
      tags = "Menu",
      summary = "Get a menu",
      description =
          "By passing the menu id, you can get access to available categories and items in the menu",
      operationId = "GetMenuV2")
  @ApiResponse(
      responseCode = "200",
      description = "Menu",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = MenuDTO.class)))
  @ReadAPIResponses
  ResponseEntity<MenuDTO> getMenu(
      @PathVariable(name = "id") UUID id,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId) {
    Menu menu =
        this.menuQueryService
            .findById(id)
            .orElseThrow(() -> new MenuNotFoundException(new MenuCommand(correlationId, id)));
    return ResponseEntity.ok(menuMapper.toDto(menu));
  }
}
