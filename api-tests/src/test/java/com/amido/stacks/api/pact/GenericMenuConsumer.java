package com.amido.stacks.api.pact;

import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class GenericMenuConsumer {

  @Rule
  public PactProviderRuleMk2 mockProvider =
      new PactProviderRuleMk2("JavaMenuAPI", "localhost", 8080, this);

  private final RestTemplate restTemplate = new RestTemplate();

  private PactDslJsonBody expectedSearchByTermResults() {
    PactDslJsonBody response = new PactDslJsonBody();
    response.integerType("pageSize", 20).integerType("pageNumber", 1).array("menu");
    return response;
  }

  private PactDslJsonBody expectedMenu() {
    PactDslJsonBody response = new PactDslJsonBody();
    response
        .uuid("id")
        .uuid("tenantId")
        .stringType("name", "Dessert Menu (Automated Test Data)")
        .stringType("description")
        .booleanType("enabled");
    return response;
  }

  private PactDslJsonBody expectedCreateMenuResponse() {
    PactDslJsonBody response = new PactDslJsonBody();
    response.uuid("id");
    return response;
  }

  private PactDslJsonBody genericCategory() {
    PactDslJsonBody response = new PactDslJsonBody();
    response
        .uuid("id")
        .stringType("name", "Cheese Cakes (Automated Test Data)")
        .stringType("description", "category description")
        .array("items");
    return response;
  }

  private PactDslJsonBody genericItem() {
    PactDslJsonBody response = new PactDslJsonBody();
    response
        .uuid("id")
        .stringType("name", "Coconut Cheese Cake (Automated Test Data)")
        .stringType("description", "item description")
        .booleanType("available");
    return response;
  }

  @Pact(consumer = "JavaAutomatedTestsMenuConsumer")
  public RequestResponsePact createPact(PactDslWithProvider builder) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");

    return builder
        .given("Get all menus")
        .uponReceiving("GET REQUEST")
        .path("/v1/menu")
        .method("GET")
        .willRespondWith()
        .status(200)
        .headers(headers)
        .body("{\"pageSize\":20,\"pageNumber\":1}")
        .given("Create a new menu")
        .uponReceiving("POST REQUEST")
        .method("POST")
        .headers(headers)
        .body(expectedMenu())
        .path("/v1/menu")
        .willRespondWith()
        .body(expectedCreateMenuResponse())
        .status(201)
        .given("Search menu by 'search term'")
        .uponReceiving("GET REQUEST BY TERM")
        .method("GET")
        .headers(headers)
        .path("/v1/menu")
        .query("searchTerm=Dessert Menu (Automated Test Data)")
        .willRespondWith()
        .status(200)
        .body(expectedSearchByTermResults())
        .toPact();
  }

  @Test
  @PactVerification()
  public void givenGet_whenSendRequest_shouldReturn200WithProperHeaderAndBody() {
    // get all
    ResponseEntity<String> response =
        new RestTemplate().getForEntity(mockProvider.getUrl() + "/v1/menu", String.class);

    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getHeaders().get("Content-Type").contains("application/json")).isTrue();
    assertThat(response.getBody()).contains("pageSize", "20", "pageNumber", "1");

    // post
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    String jsonBody = expectedMenu().toString();

    ResponseEntity<String> postResponse =
        restTemplate.exchange(
            mockProvider.getUrl() + "/v1/menu",
            HttpMethod.POST,
            new HttpEntity<>(jsonBody, httpHeaders),
            String.class);

    assertThat(postResponse.getStatusCode().value()).isEqualTo(201);

    // searchTerm
    ResponseEntity<String> getByTermResponse =
        restTemplate.exchange(
            mockProvider.getUrl() + "/v1/menu?searchTerm=Dessert Menu (Automated Test Data)",
            HttpMethod.GET,
            new HttpEntity<>(httpHeaders),
            String.class);

    assertThat(getByTermResponse.getStatusCode().value()).isEqualTo(200);
    assertThat(getByTermResponse.getBody()).contains("pageSize", "pageNumber");
    assertThat(getByTermResponse.getBody().contains("Dessert Menu (Automated Test Data)"));
  }
}
