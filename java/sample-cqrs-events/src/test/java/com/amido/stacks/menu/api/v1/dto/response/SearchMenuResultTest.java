package com.amido.stacks.menu.api.v1.dto.response;

import com.amido.workloads.menu.api.v1.dto.response.SearchMenuResult;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class SearchMenuResultTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(SearchMenuResult.class).verify();
  }
}
