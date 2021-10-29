package com.amido.stacks.core.api.auth.impl;

import com.amido.stacks.core.api.auth.AuthController;
import com.amido.stacks.core.api.dto.request.GenerateTokenRequest;
import com.amido.stacks.core.api.dto.response.GenerateTokenResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthControllerImpl implements AuthController {

  @Value("${auth.resource.url}")
  private String resourceUrl;

  private final RestTemplate restTemplate;

  public AuthControllerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public ResponseEntity<GenerateTokenResponse> generateToken(@Valid GenerateTokenRequest request) {

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);

    GenerateTokenRequest requestBody = new GenerateTokenRequest();
    requestBody.setClient_id(request.getClient_id());
    requestBody.setClient_secret(request.getClient_secret());
    requestBody.setAudience(request.getAudience());
    requestBody.setGrant_type(request.getGrant_type());

    HttpEntity<GenerateTokenRequest> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
    ResponseEntity<GenerateTokenResponse> responseEntity =
        restTemplate.exchange(
            resourceUrl, HttpMethod.POST, requestEntity, GenerateTokenResponse.class);

    GenerateTokenResponse generateTokenResponse = responseEntity.getBody();

    return new ResponseEntity<>(generateTokenResponse, responseEntity.getStatusCode());
  }
}
