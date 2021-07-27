@ignore
Feature: Create reusable data

  Scenario: Create Object
    Given url __arg.url
    And header Authorization = auth.bearer_token
    And request __arg.body
    When method POST
    Then status 201
    * def id = response.id
    * print id.concat(' menu was created')
