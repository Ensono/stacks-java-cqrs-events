@AWS
Feature: Check whether AWS features are operational
  The `/v1/secrets` endpoint returns a few example secrets to enable us to verify the connection

  Scenario: Example Secrets endpoint
    Given url base_url.concat(secrets)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    * match $ == "Secrets -> SECRET-VALUE-1, SECRET-VALUE-2, SECRET-VALUE-3, SECRET-VALUE-4"
