package com.amido.stacks.api.status;

import com.amido.stacks.api.WebServiceEndPoints;
import io.restassured.RestAssured;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class ApplicationStatus {

  final String BASE_URL = WebServiceEndPoints.BASE_URL.getUrl();

  public AppStatus currentStatus() {
    String path = BASE_URL + WebServiceEndPoints.STATUS.getUrl();
    int statusCode = RestAssured.get(path).statusCode();

    return (statusCode == 200) ? AppStatus.RUNNING : AppStatus.DOWN;
  }

  @Step("Get current status message")
  public void readStatusMessage() {
    SerenityRest.get(BASE_URL + WebServiceEndPoints.STATUS.getUrl());
  }
}
