package com.amido.stacks.workloads.menu.api.v1.impl;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.amido.stacks.workloads.menu.api.v1.QueryMenuController;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.workloads.menu.commands.MenuCommand;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.service.MenuQueryService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryMenuControllerImpl implements QueryMenuController {

  Logger logger = LoggerFactory.getLogger(QueryMenuControllerImpl.class);

  @Autowired private SearchMenuResultItemMapper searchMenuResultItemMapper;

  @Autowired private MenuMapper menuMapper;

  private MenuQueryService menuQueryService;

  public QueryMenuControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<SearchMenuResult> searchMenu(
      String searchTerm, UUID restaurantId, Integer pageSize, Integer pageNumber) {
    List<Menu> menuList;

    if (isNotEmpty(searchTerm) && nonNull(restaurantId)) {
      menuList =
          this.menuQueryService.findAllByRestaurantIdAndNameContaining(
              restaurantId, searchTerm, pageSize, pageNumber);
    } else if (isNotEmpty(searchTerm)) {
      menuList = this.menuQueryService.findAllByNameContaining(searchTerm, pageSize, pageNumber);
    } else if (nonNull(restaurantId)) {
      menuList = this.menuQueryService.findAllByRestaurantId(restaurantId, pageSize, pageNumber);
    } else {
      menuList = this.menuQueryService.findAll(pageNumber, pageSize);
    }

    return ResponseEntity.ok(
        new SearchMenuResult(
            pageSize,
            pageNumber,
            menuList.stream()
                .map(m -> searchMenuResultItemMapper.toDto(m))
                .collect(Collectors.toList())));
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
