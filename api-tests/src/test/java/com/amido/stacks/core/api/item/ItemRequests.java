package com.amido.stacks.core.api.item;

import com.amido.stacks.core.api.WebServiceEndPoints;
import java.util.HashMap;
import java.util.Map;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class ItemRequests {

  private static String menuUrl =
      WebServiceEndPoints.BASE_URL.getUrl().concat(WebServiceEndPoints.MENU.getUrl());
  private static String authorizationToken;
  private static EnvironmentVariables environmentVariables =
      SystemEnvironmentVariables.createEnvironmentVariables();

  private static String generateAuthorisation =
      EnvironmentSpecificConfiguration.from(environmentVariables)
          .getProperty("generate.auth0.token");

  boolean generateToken = Boolean.parseBoolean(generateAuthorisation);
  private static final Map<String, String> commonHeaders = new HashMap<>();

  public ItemRequests() {
    authorizationToken = String.valueOf(Serenity.getCurrentSession().get("Access Token"));

    if (generateToken) {
      commonHeaders.put("Authorization", "Bearer " + authorizationToken);
    }
  }

  @Step("Create a new item")
  public void createItem(String body, String menuID, String categoryID) {
    SerenityRest.given()
        .headers(commonHeaders)
        .contentType("application/json")
        .body(body)
        .when()
        .post(
            menuUrl
                .concat("/")
                .concat(menuID)
                .concat(WebServiceEndPoints.CATEGORY.getUrl())
                .concat("/")
                .concat(categoryID)
                .concat(WebServiceEndPoints.ITEMS.getUrl()));
  }

  @Step("Update the item")
  public void updateItem(String body, String menuID, String categoryID, String itemID) {
    SerenityRest.given()
        .headers(commonHeaders)
        .contentType("application/json")
        .body(body)
        .when()
        .put(
            menuUrl
                .concat("/")
                .concat(menuID)
                .concat(WebServiceEndPoints.CATEGORY.getUrl())
                .concat("/")
                .concat(categoryID)
                .concat(WebServiceEndPoints.ITEMS.getUrl())
                .concat("/")
                .concat(itemID));
  }

  @Step("Delete the item")
  public void deleteTheItem(String menuID, String categoryID, String itemID) {
    SerenityRest.given()
        .headers(commonHeaders)
        .contentType("application/json")
        .when()
        .delete(
            menuUrl
                .concat("/")
                .concat(menuID)
                .concat(WebServiceEndPoints.CATEGORY.getUrl())
                .concat("/")
                .concat(categoryID)
                .concat(WebServiceEndPoints.ITEMS.getUrl())
                .concat("/")
                .concat(itemID));
  }
}
