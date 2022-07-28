package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.workloads.menu.service.v1.SecretsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/v1/secrets",
    produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
@RequiredArgsConstructor
public class SecretsController {

  private final SecretsService secretsService;

  @GetMapping
  public ResponseEntity<String> getSecrets() {

    return ResponseEntity.ok(secretsService.getSecrets());
  }
}
