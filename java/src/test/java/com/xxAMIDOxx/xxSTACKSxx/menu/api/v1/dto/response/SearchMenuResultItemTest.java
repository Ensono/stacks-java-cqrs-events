package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class SearchMenuResultItemTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(SearchMenuResultItem.class).verify();
  }
}
