package com.amido.stacks.tests.api.cloud;

import com.amido.stacks.tests.api.WebServiceEndPoints;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class AwsFeaturesStatus {

  final String BASE_URL = WebServiceEndPoints.BASE_URL.getUrl();

  public String expectedExampleSecrets =
      "Secrets -> SECRET-VALUE-1, SECRET-VALUE-2, SECRET-VALUE-3, SECRET-VALUE-4";

  @Step("Get current example secrets")
  public void readExampleSecrets() {
    SerenityRest.get(BASE_URL + WebServiceEndPoints.SECRETS.getUrl());
  }
}
