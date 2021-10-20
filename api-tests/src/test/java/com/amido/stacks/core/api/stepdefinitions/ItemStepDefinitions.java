package com.amido.stacks.core.api.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

import com.amido.stacks.core.api.ExceptionMessages;
import com.amido.stacks.core.api.item.ItemActions;
import com.amido.stacks.core.api.item.ItemRequests;
import com.amido.stacks.core.api.menu.MenuActions;
import com.amido.stacks.core.api.menu.MenuRequests;
import com.amido.stacks.core.api.models.Item;
import com.amido.stacks.core.api.models.Menu;
import com.amido.stacks.core.api.templates.FieldValues;
import com.amido.stacks.core.api.templates.MergeFrom;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

public class ItemStepDefinitions {

  @Steps
  ItemRequests itemRequest;

  @Steps
  MenuRequests menuRequest;

  @Steps
  MenuActions menuActions;

  String menuId;
  String categoryId;
  String itemBody;
  String itemId;

  @Given("the following item data:")
  public void the_following_item_data(List<Map<String, String>> itemDetails) throws IOException {
    itemBody =
        MergeFrom.template("templates/item.json")
            .withDefaultValuesFrom(FieldValues.in("templates/standard-item.properties"))
            .withFieldsFrom(itemDetails.get(0));

    Serenity.setSessionVariable("Item").to(itemBody);
  }

  @When("I create an item for the following menu and category:")
  public void create_item_for_menu_and_category(DataTable table) {
    List<List<String>> data = table.asLists(String.class);
    String menu_id = data.get(0).get(1);
    String category_id = data.get(1).get(1);

    itemRequest.createItem(itemBody, menu_id, category_id);
  }

  @When("I create an item for the following {string} menu and {string} category")
  public void create_item_for_menu_and_category(String menu_id, String category_id) {
    itemRequest.createItem(itemBody, menu_id, category_id);
  }

  @When("I create an item for the menu with {string} id")
  public void create_item_for_given_menu(String menu_id) {
    create_item_for_menu_and_category(
        menu_id, Serenity.getCurrentSession().get("CategoryId").toString());
  }

  @When("I create an item for the category with {string} id")
  public void create_item_for_given_category(String category_id) {
    create_item_for_menu_and_category(
        Serenity.getCurrentSession().get("MenuId").toString(), category_id);
  }

  @When("I create an item for the previous menu and category")
  public void create_item_for_menu_and_category() {
    menuId = Serenity.getCurrentSession().get("MenuId").toString();
    categoryId = Serenity.getCurrentSession().get("CategoryId").toString();
    itemRequest.createItem(itemBody, menuId, categoryId);
  }

  @When("I delete the created item")
  public void i_delete_the_last_created_item() {
    itemRequest.deleteTheItem(menuId, categoryId, itemId);
  }

  @When("I delete the item for the menu with {string} id")
  public void i_delete_the_item_for_given_menuId(String menu_id) {
    itemRequest.deleteTheItem(menu_id, categoryId, itemId);
  }

  @When("I delete the item for the category with {string} id")
  public void i_delete_the_item_for_given_categoryId(String category_id) {
    itemRequest.deleteTheItem(menuId, category_id, itemId);
  }

  @When("the item is successfully deleted")
  public void the_category_was_successfully_deleted() {
    menuRequest.getMenu(menuId);
    Menu actualMenu = menuActions.responseToMenu(lastResponse());

    Assert.assertEquals(0, actualMenu.getCategories().get(0).getItems().size());
  }

  @Then("the item was successfully created")
  public void the_item_was_successfully_created() {
    restAssuredThat(response -> response.statusCode(201));
    itemId = menuActions.getIdOfLastCreatedObject();
  }

  @Then("the created item should include the following data:")
  public void the_created_item_contain_correct_data(List<Map<String, String>> itemDetails) {
    menuRequest.getMenu(menuId);

    Item expectedItem = ItemActions.mapToItem(itemDetails.get(0), itemId);
    Menu actualMenu = menuActions.responseToMenu(lastResponse());

    List<Item> actualItems =
        actualMenu.getCategories().stream()
            .filter(cat -> cat.getId().equals(categoryId))
            .flatMap(category -> category.getItems().stream())
            .collect(Collectors.toList());

    Assert.assertTrue(actualItems.contains(expectedItem));
  }

  @Then("the item should include the following data:")
  public void the_updated_item_contain_correct_data(List<Map<String, String>> itemDetails) {
    the_created_item_contain_correct_data(itemDetails);
  }

  @Then("the 'item already exists' message is returned")
  public void i_check_the_menu_already_exist_message() {
    menuActions.check_exception_message(ExceptionMessages.ITEM_ALREADY_EXISTS, lastResponse());
  }

  @Then("the 'item does not exist' message is returned")
  public void i_check_the_menu_does_not_exist_message() {
    menuActions.check_exception_message(ExceptionMessages.ITEM_DOES_NOT_EXIST, lastResponse());
  }

  @When("I delete the item with {string} id")
  public void i_delete_item_by_id(String item_id) {
    String menu_id = String.valueOf(Serenity.getCurrentSession().get("MenuId"));
    String category_id = String.valueOf(Serenity.getCurrentSession().get("CategoryId"));

    itemRequest.deleteTheItem(menu_id, category_id, item_id);
  }

  @Then("I update the item with the following data:")
  public void i_update_the_menu_with_following_data(List<Map<String, String>> itemDetails)
      throws IOException {
    menuId = Serenity.getCurrentSession().get("MenuId").toString();
    categoryId = Serenity.getCurrentSession().get("CategoryId").toString();
    the_following_item_data(itemDetails);

    itemRequest.updateItem(itemBody, menuId, categoryId, itemId);
  }

  @Then("I update the item with the following data for the menu with {string} id:")
  public void i_update_the_menu_for_given_menu(
      String menu_id, List<Map<String, String>> itemDetails) throws IOException {
    the_following_item_data(itemDetails);
    itemRequest.updateItem(itemBody, menu_id, categoryId, itemId);
  }

  @Then("I update the item with the following data for the category with {string} id:")
  public void i_update_the_menu_for_given_category(
      String category_id, List<Map<String, String>> itemDetails) throws IOException {
    the_following_item_data(itemDetails);
    itemRequest.updateItem(itemBody, menuId, category_id, itemId);
  }
}
