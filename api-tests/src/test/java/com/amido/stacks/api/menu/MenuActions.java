package com.amido.stacks.api.menu;

import static net.serenitybdd.rest.SerenityRest.lastResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amido.stacks.api.ExceptionMessages;
import com.amido.stacks.api.OAuthConfigurations;
import com.amido.stacks.api.models.AuthorizationRequest;
import com.amido.stacks.api.models.Menu;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuActions {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuActions.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static String client_id = OAuthConfigurations.CLIENT_ID.getOauthConfiguration();
  private static String client_secret = OAuthConfigurations.CLIENT_SECRET.getOauthConfiguration();
  private static String audience = OAuthConfigurations.AUDIENCE.getOauthConfiguration();
  private static String grant_type = OAuthConfigurations.GRANT_TYPE.getOauthConfiguration();
  private static EnvironmentVariables environmentVariables =
      SystemEnvironmentVariables.createEnvironmentVariables();
  private static String generateAuthorisation =
      EnvironmentSpecificConfiguration.from(environmentVariables)
          .getProperty("generate.auth0.token");
  private static String authBody;

  static {
    try {
      authBody =
          objectMapper.writeValueAsString(
              new AuthorizationRequest(client_id, client_secret, audience, grant_type));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public String getIdOfLastCreatedObject() {
    return String.valueOf(toJson(lastResponse().getBody().prettyPrint()).get("id"));
  }

  public static void getAuthToken() {
    boolean generateToken = Boolean.parseBoolean(generateAuthorisation);

    if (generateToken) {
      MenuRequests.getAuthorizationToken(authBody);
      String accessToken = lastResponse().jsonPath().get("access_token").toString();
      Serenity.setSessionVariable("Access Token").to(accessToken);

      if (accessToken.isEmpty()) {
        LOGGER.error("The access token could not be obtained");
      }
    } else {
      Serenity.setSessionVariable("Access Token").to("");
    }
  }

  public String getRestaurantIdOfLastCreatedMenu() {
    return String.valueOf(toJson(lastResponse().getBody().prettyPrint()).get("restaurantId"));
  }

  public void check_exception_message(ExceptionMessages parameter, Response response) {
    JSONObject js = toJson(response.getBody().prettyPrint());
    String message = parameter.getMessage();

    Assert.assertTrue(js.get("description").toString().matches(message));
  }

  public static JSONObject toJson(String stringToParse) {
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(stringToParse);
    } catch (JSONException err) {
      LOGGER.warn(err.getMessage());
    }
    return jsonObject;
  }

  public Menu responseToMenu(Response response) {
    Menu actualMenu = null;
    try {
      actualMenu = objectMapper.readValue(response.body().print(), Menu.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return actualMenu;
  }

  public static Menu mapToMenu(Map<String, String> properties, String id) {
    return new Menu(
        id,
        UUID.fromString(properties.get("tenantId")),
        properties.get("name"),
        properties.get("description"),
        null,
        Boolean.parseBoolean(properties.get("enabled")));
  }

  public static String createUrlWithCriteria(List<List<String>> data) {
    StringBuilder finalPath = new StringBuilder();
    for (int i = 0; i < data.size(); i++) {

      String parameter = data.get(i).get(0);
      String value = data.get(i).get(1);

      finalPath.append(parameter).append("=").append(value);
      if (i != data.size() - 1) {
        finalPath.append("&");
      }
    }
    return finalPath.toString();
  }
}
