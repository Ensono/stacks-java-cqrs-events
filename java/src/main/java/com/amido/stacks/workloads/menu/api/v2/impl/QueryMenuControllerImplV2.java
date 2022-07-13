package com.amido.stacks.workloads.menu.api.v2.impl;

import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v2.QueryMenuControllerV2;
import com.amido.stacks.workloads.menu.commands.MenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.service.MenuQueryService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryMenuControllerImplV2 implements QueryMenuControllerV2 {

  @Autowired private MenuMapper menuMapper;

  private MenuQueryService menuQueryService;

  public QueryMenuControllerImplV2(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<MenuDTO> getMenu(UUID id, String correlationId) {
    Menu menu =
        this.menuQueryService
            .findById(id)
            .orElseThrow(() -> new MenuNotFoundException(new MenuCommand(correlationId, id)));
    return ResponseEntity.ok(menuMapper.toDto(menu));
  }
}
