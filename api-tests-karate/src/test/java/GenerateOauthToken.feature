@ignore
Feature: Generate auth0 token

  Scenario: Generate access token
    * set oauth_body
      | path          | value               |
      | client_id     | client_id_value     |
      | client_secret | client_secret_value |
      | audience      | audience_value      |
      | grant_type    | grant_type_value    |
    Given url oauth_token_url
    And request oauth_body
    When method POST
    Then status 200

    * def token = response.access_token
    * def token_type = response.token_type
