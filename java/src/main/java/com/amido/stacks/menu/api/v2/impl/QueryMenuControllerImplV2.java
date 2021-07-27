package com.amido.stacks.menu.api.v2.impl;

import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v2.QueryMenuControllerV2;
import com.amido.stacks.menu.commands.MenuCommand;
import com.amido.stacks.menu.commands.OperationCode;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.exception.MenuNotFoundException;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import com.amido.stacks.menu.service.MenuQueryService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryMenuControllerImplV2 implements QueryMenuControllerV2 {

  private DomainToDtoMapper mapper;

  private MenuQueryService menuQueryService;

  public QueryMenuControllerImplV2(DomainToDtoMapper mapper, MenuQueryService menuQueryService) {
    this.mapper = mapper;
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<MenuDTO> getMenu(UUID id, String correlationId) {
    Menu menu =
        this.menuQueryService
            .findById(id)
            .orElseThrow(
                () ->
                    new MenuNotFoundException(
                        new MenuCommand(OperationCode.GET_MENU_BY_ID, correlationId, id)));
    return ResponseEntity.ok(mapper.toMenuDto(menu));
  }
}
