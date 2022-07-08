package com.amido.stacks.workloads.menu.mappers;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ItemDTO;
import com.amido.stacks.workloads.menu.domain.Item;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ItemMapper extends BaseMapper<ItemDTO, Item> {}
