package com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

import com.xxAMIDOxx.xxSTACKSxx.api.ExceptionMessages;
import com.xxAMIDOxx.xxSTACKSxx.api.WebServiceEndPoints;
import com.xxAMIDOxx.xxSTACKSxx.api.category.CategoryActions;
import com.xxAMIDOxx.xxSTACKSxx.api.category.CategoryRequests;
import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuActions;
import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuRequests;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Category;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Menu;
import com.xxAMIDOxx.xxSTACKSxx.api.templates.FieldValues;
import com.xxAMIDOxx.xxSTACKSxx.api.templates.MergeFrom;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

public class CategoryStepDefinitions {

  @Steps private CategoryRequests categoryRequests;

  @Steps private MenuRequests menuRequests;

  @Steps private MenuActions menuActions;

  private final String MENU_URL =
      WebServiceEndPoints.BASE_URL.getUrl() + WebServiceEndPoints.MENU.getUrl();

  private String categoryBody;
  private String menuId;
  private String categoryId;

  @When("the following category data:")
  public void create_the_category_body(List<Map<String, String>> categoryDetails)
      throws IOException {
    categoryBody =
        MergeFrom.template("templates/category.json")
            .withDefaultValuesFrom(FieldValues.in("templates/standard-category.properties"))
            .withFieldsFrom(categoryDetails.get(0));

    Serenity.setSessionVariable("CategoryBody").to(categoryBody);
  }

  @When("I create a new category for the existing menu")
  public void i_create_the_category_for_existing_menu() {
    menuId = Serenity.getCurrentSession().get("MenuId").toString();
    categoryRequests.createCategory(categoryBody, menuId);
  }

  @Then("the category was successfully created")
  public void the_category_was_successfully_created() {
    restAssuredThat(response -> response.statusCode(201));
    categoryId = menuActions.getIdOfLastCreatedObject();
    Serenity.setSessionVariable("CategoryId").to(categoryId);
  }

  @Then("I update the category with the following data:")
  public void i_update_the_category_with_following_data(List<Map<String, String>> menuDetails)
      throws IOException {
    menuId = Serenity.getCurrentSession().get("MenuId").toString();
    categoryId = Serenity.getCurrentSession().get("CategoryId").toString();
    create_the_category_body(menuDetails);

    categoryRequests.updateCategory(categoryBody, menuId, categoryId);
  }

  @Then("I update the category for the menu with {string} id with the following data:")
  public void update_the_category_for_menu(String menu_id, List<Map<String, String>> menuDetails)
      throws IOException {
    create_the_category_body(menuDetails);

    categoryRequests.updateCategory(
        categoryBody, menu_id, Serenity.getCurrentSession().get("CategoryId").toString());
  }

  @Then("I update the category with {string} id with the following data:")
  public void update_the_category_for_given_id(
      String category_id, List<Map<String, String>> menuDetails) throws IOException {
    create_the_category_body(menuDetails);
    categoryRequests.updateCategory(categoryBody, menuId, category_id);
  }

  @Then("the menu should include the following category data:")
  public void the_menu_contains_the_following_category_data(List<Map<String, String>> menuDetails) {
    the_category_should_include_the_following_data(menuDetails);
  }

  @When("I create a new category for the menu with {string} id")
  public void i_create_a_new_category_for_the_menu_with_id(String menu_id) {
    categoryRequests.createCategory(categoryBody, menu_id);
  }

  @When("I delete the created category")
  public void i_delete_the_last_created_category() {
    categoryRequests.deleteTheCategory(menuId, categoryId);
  }

  @When("I delete the category with {string} id")
  public void i_delete_category_by_id(String id) {
    String menu_id = String.valueOf(Serenity.getCurrentSession().get("MenuId"));
    categoryRequests.deleteTheCategory(menu_id, id);
  }

  @When("I delete the category with {string} id for {string} menu")
  public void i_delete_category_by_id(String category_id, String menu_id) {
    categoryRequests.deleteTheCategory(menu_id, category_id);
  }

  @When("the category is successfully deleted")
  public void the_category_was_successfully_deleted() {
    menuRequests.getMenu(menuId);
    Menu actualMenu = menuActions.responseToMenu(lastResponse());

    Assert.assertEquals(0, actualMenu.getCategories().size());
  }

  @Then("the 'category does not exist' message is returned")
  public void i_check_the_category_does_not_exist_message() {
    menuActions.check_exception_message(ExceptionMessages.CATEGORY_DOES_NOT_EXIST, lastResponse());
  }

  @Then("the 'category already exists' message is returned")
  public void i_check_the_category_already_exists_message() {
    menuActions.check_exception_message(ExceptionMessages.CATEGORY_ALREADY_EXISTS, lastResponse());
  }

  @Then("the created category should include the following data:")
  public void the_category_should_include_the_following_data(
      List<Map<String, String>> categoryDetails) {
    MenuRequests.getMenuByParam(menuId);
    Category expectedCategory = CategoryActions.mapToCategory(categoryDetails.get(0), categoryId);

    Menu actualMenu = menuActions.responseToMenu(lastResponse());
    Assert.assertTrue(actualMenu != null && actualMenu.getCategories().contains(expectedCategory));
  }
}
