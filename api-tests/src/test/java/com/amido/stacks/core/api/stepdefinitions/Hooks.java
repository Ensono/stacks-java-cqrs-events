package com.amido.stacks.core.api.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

import com.amido.stacks.core.api.menu.MenuActions;
import com.amido.stacks.core.api.menu.MenuRequests;
import com.amido.stacks.core.api.models.Menu;
import com.amido.stacks.core.api.models.ResponseWrapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import java.util.List;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.cucumber.suiteslicing.SerenityTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

  private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);
  private static boolean firstTestRun = false;

  public static void deleteAllMenusFromPreviousRun() {
    MenuRequests.getMenusBySearchTerm("(Automated Test Data)");
    ResponseWrapper responseWrapper = lastResponse().body().as(ResponseWrapper.class);
    List<Menu> listOfMenusToDelete = responseWrapper.getResults();

    for (Menu currentMenu : listOfMenusToDelete) {
      MenuRequests.deleteTheMenu(currentMenu.getId());
      restAssuredThat(response -> response.statusCode(200));

      LOGGER.info(
          String.format("The menu with '%s' id was successfully deleted.", currentMenu.getId()));
    }
  }

  @Before
  public void before() {
    SerenityTags.create().tagScenarioWithBatchingInfo();
  }

  @Before
  public static void beforeAll() {
    if (!firstTestRun) {
      LOGGER.info("Get the Authorization Token");
      MenuActions.getAuthToken();

      System.out.println("Delete all data from previous automated tests:");
      deleteAllMenusFromPreviousRun();
      firstTestRun = true;
    }
  }

  @After("@DeleteCreatedMenu")
  public void afterFirst() {
    String menuId = String.valueOf(Serenity.getCurrentSession().get("MenuId"));
    if (!menuId.isEmpty()) {
      MenuRequests.deleteTheMenu(menuId);
      LOGGER.info(String.format("The menu with '%s' id was successfully deleted.", menuId));
    }
  }
}
