package com.amido.stacks.workloads.menu.mappers.commands;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.commands.CreateCategoryCommand;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CreateCategoryMapper
    extends BaseMapper<CreateCategoryRequest, CreateCategoryCommand> {}
