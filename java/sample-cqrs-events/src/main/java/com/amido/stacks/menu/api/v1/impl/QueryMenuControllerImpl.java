package com.amido.stacks.menu.api.v1.impl;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.amido.stacks.menu.api.v1.QueryMenuController;
import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.menu.commands.MenuCommand;
import com.amido.stacks.menu.commands.OperationCode;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.exception.MenuNotFoundException;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import com.amido.stacks.menu.service.MenuQueryService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** MenuControllerImpl - MenuDTO Controller used to interact and manage menus API. */
@RestController
public class QueryMenuControllerImpl implements QueryMenuController {

  Logger logger = LoggerFactory.getLogger(QueryMenuControllerImpl.class);

  private DomainToDtoMapper mapper;

  private MenuQueryService menuQueryService;

  public QueryMenuControllerImpl(DomainToDtoMapper mapper, MenuQueryService menuQueryService) {
    this.mapper = mapper;
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<SearchMenuResult> searchMenu(
      final String searchTerm,
      final UUID restaurantId,
      final Integer pageSize,
      final Integer pageNumber) {
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
                .map(m -> mapper.toSearchMenuResultItem(m))
                .collect(Collectors.toList())));
  }

  @Override
  public ResponseEntity<MenuDTO> getMenu(final UUID id, final String correlationId) {
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
