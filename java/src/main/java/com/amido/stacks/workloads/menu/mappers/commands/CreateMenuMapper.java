package com.amido.stacks.workloads.menu.mappers.commands;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.commands.CreateMenuCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CreateMenuMapper extends BaseMapper<CreateMenuRequest, CreateMenuCommand> {

  @Override
  @Mapping(source = "restaurantId", target = "tenantId")
  CreateMenuRequest toDto(CreateMenuCommand command);

  @Override
  @Mapping(source = "tenantId", target = "restaurantId")
  CreateMenuCommand fromDto(CreateMenuRequest request);
}
