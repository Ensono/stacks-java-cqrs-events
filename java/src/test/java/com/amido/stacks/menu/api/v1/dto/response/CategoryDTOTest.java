package com.amido.stacks.menu.api.v1.dto.response;

import com.amido.workloads.menu.api.v1.dto.response.CategoryDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class CategoryDTOTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.simple().forClass(CategoryDTO.class).verify();
  }
}
