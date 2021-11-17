package com.amido.workloads.menu.api.v2.impl;

import com.amido.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.workloads.menu.api.v2.QueryMenuControllerV2;
import com.amido.workloads.menu.commands.MenuCommand;
import com.amido.workloads.menu.domain.Menu;
import com.amido.workloads.menu.exception.MenuNotFoundException;
import com.amido.workloads.menu.mappers.DomainToDtoMapper;
import com.amido.workloads.menu.service.MenuQueryService;
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
            .orElseThrow(() -> new MenuNotFoundException(new MenuCommand(correlationId, id)));
    return ResponseEntity.ok(mapper.toMenuDto(menu));
  }
}
