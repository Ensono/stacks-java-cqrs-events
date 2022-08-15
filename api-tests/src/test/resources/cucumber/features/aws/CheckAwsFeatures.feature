Feature: Check whether AWS features are operational
  The `/v1/secrets` endpoint returns a few example secrets to enable us to verify the connection

  @AWS
  Scenario: Example Secrets endpoint
    Given the application is running
    When I check the example secrets
    Then the API should return the correct examples
