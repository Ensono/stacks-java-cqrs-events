package com.amido.stacks.workloads.menu.mappers.commands;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.stacks.workloads.menu.commands.UpdateItemCommand;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UpdateItemMapper extends BaseMapper<UpdateItemRequest, UpdateItemCommand> {}
