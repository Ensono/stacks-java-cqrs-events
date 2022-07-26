package com.amido.stacks.workloads.menu.mappers.cqrs;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.core.mapping.MapperUtils;
import com.amido.stacks.workloads.menu.commands.UpdateCategoryCommand;
import com.amido.stacks.workloads.menu.domain.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {MapperUtils.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UpdateCategoryCommandMapper extends BaseMapper<UpdateCategoryCommand, Category> {

  @Override
  @Mapping(source = "id", target = "categoryId")
  UpdateCategoryCommand toDto(Category category);

  @Override
  @Mapping(source = "categoryId", target = "id")
  Category fromDto(UpdateCategoryCommand request);
}
