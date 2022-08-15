package com.amido.stacks.tests.api.stepdefinitions;

import com.amido.stacks.tests.api.status.AwsFeaturesStatus;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import org.hamcrest.Matchers;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AwsStepDefinitions {

  @Steps AwsFeaturesStatus AwsFeaturesStatus;

  @When("I check the example secrets")
  public void check_the_example_secrets() {
    AwsFeaturesStatus.readStatusMessage();
  }

  @Then("the API should return the correct examples")
  public void the_API_should_return() {
    restAssuredThat(lastResponse -> lastResponse.body(equalTo(AwsFeaturesStatus.expectedExampleSecrets)));
  }

}
