package com.amido.stacks.workloads.menu.api.v1;

import static org.assertj.core.api.BDDAssertions.then;

import com.amido.stacks.workloads.Application;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.util.TestHelper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class,
    properties = {
      "stacks-secret-1=SEC1",
      "stacks-secret-2=SEC2",
      "stacks-secret-3=SEC3",
      "stacks-secret-4=SEC4"
    })
@TestPropertySource(
    properties = {
      "management.port=0",
      "aws.xray.enabled=false",
      "aws.secretsmanager.enabled=false",
      "cosmos.enabled=false"
    })
@Tag("Integration")
@ActiveProfiles("test")
class SecretsControllerTest {

  public static final String GET_SECRETS = "/v1/secrets";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepository menuRepository;

  @Test
  void shouldReturnValidSecrets() {
    // Given

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format("%s/v1/secrets", TestHelper.getBaseURL(port)), String.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(response.getBody()).isEqualTo("Secrets -> SEC1, SEC2, SEC3, SEC4");
  }
}
