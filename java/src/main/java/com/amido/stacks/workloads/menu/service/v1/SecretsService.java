package com.amido.stacks.workloads.menu.service.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecretsService {

  @Value(value = "${stacks-secret-1:secret-not-available}")
  private String secret1;

  @Value(value = "${stacks-secret-2:secret-not-available}")
  private String secret2;

  @Value(value = "${stacks-secret-3:secret-not-available}")
  private String secret3;

  @Value(value = "${stacks-secret-4:secret-not-available}")
  private String secret4;

  public String getSecrets() {

    log.info("Getting some secrets...");

    return showSecrets();
  }

  private String showSecrets() {
    return "Secrets -> " + secret1 + ", " + secret2 + ", " + secret3 + ", " + secret4;
  }
}
