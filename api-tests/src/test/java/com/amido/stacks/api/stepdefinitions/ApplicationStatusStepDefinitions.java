package com.amido.stacks.api.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.amido.stacks.api.status.AppStatus;
import com.amido.stacks.api.status.ApplicationStatus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import org.hamcrest.Matchers;

public class ApplicationStatusStepDefinitions {

  @Steps ApplicationStatus theApplication;

  @Given("the application is running")
  public void the_application_is_running() {
    assertThat(theApplication.currentStatus()).isEqualTo(AppStatus.RUNNING);
  }

  @When("I check the application status")
  public void check_the_application_status() {
    theApplication.readStatusMessage();
  }

  @Then("the API should return {string}")
  public void the_API_should_return(String expectedMessage) {
    restAssuredThat(lastResponse -> lastResponse.body(equalTo(expectedMessage)));
  }

  @Then("the API should return {string} status")
  public void the_API_return_status(String status) {
    restAssuredThat(
        lastResponse -> lastResponse.body("status", Matchers.equalToIgnoringCase(status)));
  }
}
