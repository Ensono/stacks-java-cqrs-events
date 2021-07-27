package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class MenuDTOTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.simple().forClass(MenuDTO.class).verify();
  }
}
