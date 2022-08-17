package com.amido.stacks.tests.api.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.hamcrest.Matchers.equalTo;

import com.amido.stacks.tests.api.cloud.AwsFeaturesStatus;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

public class AwsStepDefinitions {

  @Steps AwsFeaturesStatus AwsFeaturesStatus;

  @When("I check the example secrets")
  public void check_the_example_secrets() {
    AwsFeaturesStatus.readExampleSecrets();
  }

  @Then("the API should return the correct examples")
  public void the_API_should_return() {
    restAssuredThat(
        lastResponse -> lastResponse.body(equalTo(AwsFeaturesStatus.expectedExampleSecrets)));
  }
}
