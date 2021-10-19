package com.amido.stacks.core.api.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.amido.stacks.core.api.ExceptionMessages;
import com.amido.stacks.core.api.WebServiceEndPoints;
import com.amido.stacks.core.api.menu.MenuActions;
import com.amido.stacks.core.api.menu.MenuRequests;
import com.amido.stacks.core.api.models.Menu;
import com.amido.stacks.core.api.models.ResponseWrapper;
import com.amido.stacks.core.api.templates.FieldValues;
import com.amido.stacks.core.api.templates.MergeFrom;
import com.amido.stacks.core.api.templates.TemplateResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuStepDefinitions {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuStepDefinitions.class);

  @Steps MenuRequests menuRequest;

  @Steps TemplateResponse theMenuDetails;

  @Steps MenuActions menuActions;

  String menuId;
  String menuBody;
  final String BASE_URL = WebServiceEndPoints.BASE_URL.getUrl();

  @Given("the following menu data:")
  public void create_menu_body_from_data(List<Map<String, String>> menuDetails) throws IOException {
    menuBody =
        MergeFrom.template("templates/menu.json")
            .withDefaultValuesFrom(FieldValues.in("templates/standard-menu.properties"))
            .withFieldsFrom(menuDetails.get(0));

    Serenity.setSessionVariable("Menu").to(menuBody);
  }

  @Given("the menu list is not empty")
  public void get_all_existing_menus() {
    menuRequest.getAllMenus();
    restAssuredThat(response -> response.statusCode(200));
    restAssuredThat(lastResponse -> lastResponse.body("results", Matchers.notNullValue()));
  }

  @When("I create the menu")
  public void i_create_the_menu() {
    menuRequest.createMenu(menuBody);
  }

  @When("I search the created menu by id")
  public void i_search_the_last_created_menu_by_id() {
    getMenuByParameter(menuId);
  }

  public void getMenuByParameter(String parameter) {
    MenuRequests.getMenuByParam(parameter);
  }

  @When("I search the menu by:")
  public void search_the_menu_by_parameter(DataTable table) {
    List<List<String>> data = table.asLists(String.class);
    String parameter = data.get(1).get(0);
    String value = data.get(0).get(0);

    getMenuByParameter(parameter);
    Serenity.setSessionVariable(value).to(parameter);
  }

  @When("I search the menu by criteria")
  public void search_the_menu_by_multiple_parameters(DataTable table) {
    List<List<String>> data = table.asLists(String.class);
    String xPath = MenuActions.createUrlWithCriteria(data);
    String parametrisedPath = BASE_URL + WebServiceEndPoints.MENU.getUrl() + "?" + xPath;

    MenuRequests.getMenuByParametrisedPath(parametrisedPath);
  }

  @When("I get the restaurant id for last created menu")
  public void getLastCreatedRestaurantId() {
    Serenity.setSessionVariable("RestaurantId").to(menuActions.getRestaurantIdOfLastCreatedMenu());
  }

  @When("I search the menu by restaurant id")
  public void getMenuById() {
    String restaurant_id = String.valueOf(Serenity.getCurrentSession().get("RestaurantId"));
    String parametrisedPath =
        BASE_URL.concat(WebServiceEndPoints.MENU.getUrl()).concat("?restaurantId=") + restaurant_id;

    SerenityRest.get(parametrisedPath);
  }

  @Then("the {int} items are returned")
  public void the_items_are_returned(Integer expectedSize) {
    String response = SerenityRest.lastResponse().prettyPrint();

    JSONObject js = MenuActions.toJson(response);
    Assert.assertEquals(js.getJSONArray("results").length(), (int) expectedSize);
  }

  @Then("the menu should include the following details:")
  public void the_menu_contains_the_following_details(List<Map<String, String>> menuDetails) {
    Map<String, String> expectedResponse = menuDetails.get(0);
    String actualResponse = theMenuDetails.returned().getOrDefault("results", null);

    assertThat(actualResponse).contains(expectedResponse.values());
  }

  @Then("the menu should include the following data:")
  public void the_menu_contains_the_following_data(List<Map<String, String>> menuDetails) {
    restAssuredThat(response -> response.statusCode(200));

    String id = menuActions.getIdOfLastCreatedObject();
    Menu expectedMenu = MenuActions.mapToMenu(menuDetails.get(0), id);

    Menu actualMenu = lastResponse().getBody().as(Menu.class);
    assertThat(expectedMenu).isEqualToIgnoringGivenFields(actualMenu, "categories");
  }

  @When("I search the updated menu")
  public void i_search_the_updated_menu() {
    i_search_the_last_created_menu_by_id();
  }

  @Then("the menu was successfully created")
  public void the_menu_was_created() {
    restAssuredThat(response -> response.statusCode(201));
    menuId = menuActions.getIdOfLastCreatedObject();
    Serenity.setSessionVariable("MenuId").to(menuId);
  }

  @Then("the returned status code is {int}")
  public void i_check_the_status_code(int statusCode) {
    restAssuredThat(response -> response.statusCode(statusCode));
  }

  @Then("the error {string} is {string}")
  public void i_check_the_error_message(String errorField, String errorMessage) {
    restAssuredThat(status -> status.body(errorField, Matchers.equalToIgnoringCase(errorMessage)));
  }

  @Then("the 'menu does not exist' message is returned")
  public void i_check_the_menu_does_not_exist_message() {
    menuActions.check_exception_message(ExceptionMessages.MENU_DOES_NOT_EXIST, lastResponse());
  }

  @Then("the 'menu already exists' message is returned")
  public void i_check_the_menu_already_exist_message() {
    menuActions.check_exception_message(ExceptionMessages.MENU_ALREADY_EXISTS, lastResponse());
  }

  @Then("I update the menu with the following data:")
  public void i_update_the_menu_with_following_data(List<Map<String, String>> menuDetails)
      throws IOException {
    String id = menuActions.getIdOfLastCreatedObject();
    create_menu_body_from_data(menuDetails);

    menuRequest.updateMenu(menuBody, id);
  }

  @When("I update the menu for {string} id with data:")
  public void i_update_the_menu_with_data(String id, List<Map<String, String>> menuDetails)
      throws IOException {
    create_menu_body_from_data(menuDetails);
    menuRequest.updateMenu(menuBody, id);
  }

  @When("I delete the menu")
  public void i_delete_the_menu() {
    menuId = menuActions.getIdOfLastCreatedObject();
    MenuRequests.deleteTheMenu(menuId);
    LOGGER.info(String.format("The menu with '%s' id was successfully deleted.", menuId));
  }

  @When("I delete the menu with {string} id")
  public void i_delete_the_menu_by_id(String menuId) {
    MenuRequests.deleteTheMenu(menuId);
  }

  @Then("the menu is successfully deleted")
  public void the_menu_is_successfully_deleted() {
    restAssuredThat(response -> response.statusCode(200));
    menuRequest.getMenu(menuId);

    restAssuredThat(response -> response.statusCode(404));
    menuActions.check_exception_message(ExceptionMessages.MENU_DOES_NOT_EXIST, lastResponse());
  }

  @Given("the menu list is empty")
  public void the_menu_list_is_empty() {
    menuRequest.getAllMenus();
    ResponseWrapper responseWrapper = lastResponse().body().as(ResponseWrapper.class);

    restAssuredThat(response -> response.statusCode(200));
    Assert.assertEquals(0, responseWrapper.getResults().size());
  }

  @Given("I delete all existing menus")
  public void i_delete_all_existing_menus() {
    ResponseWrapper responseWrapper = lastResponse().body().as(ResponseWrapper.class);
    List<Menu> allMenus = responseWrapper.getResults();

    for (Menu currentMenu : allMenus) {
      MenuRequests.deleteTheMenu(currentMenu.getId());
      restAssuredThat(response -> response.statusCode(200));

      LOGGER.info(
          String.format("The menu with '%s' id was successfully deleted.", currentMenu.getId()));
    }
  }

  @Given("I delete all menus from previous tests")
  public void i_delete_all_existing_menus_from_test_data() {
    Hooks.deleteAllMenusFromPreviousRun();
  }

  @When("I search the created menu by id in the v2 app version")
  public void i_search_the_last_created_menu_by_id_v2() {
    getMenuByParameter_v2(menuId);
  }

  public void getMenuByParameter_v2(String parameter) {
    MenuRequests.getMenuByParam_V2(parameter);
  }

  @When("in the v2 app version I search the menu by:")
  public void search_the_menu_by_parameter_v2(DataTable table) {
    List<List<String>> data = table.asLists(String.class);
    String parameter = data.get(1).get(0);
    String value = data.get(0).get(0);

    getMenuByParameter_v2(parameter);
    Serenity.setSessionVariable(value).to(parameter);
  }
}
