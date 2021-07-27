package com.xxAMIDOxx.xxSTACKSxx.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** @author ArathyKrishna */
public class TestHelper {

  public static String getBaseURL(int port) {
    return String.format("http://localhost:%d", port);
  }

  public static HttpHeaders getRequestHttpEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
