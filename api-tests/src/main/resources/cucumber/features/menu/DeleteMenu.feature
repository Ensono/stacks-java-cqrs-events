@Functional
Feature: Delete menu

  @DeleteCreatedMenu @Smoke
  Scenario: Delete the menu
    Given the application is running
    And the following menu data:
      | name                            | description     | tenantId                             | enabled |
      | Blue Moon (Automated Test Data) | Chinese Cuisine | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201

    When I delete the menu
    Then the menu is successfully deleted


  Scenario: Delete the menu - Resource not found
    Given the application is running
    And the menu list is not empty
    When I delete the menu with "f91d2f8c-35cc-45dd-92b0-86ca548e0119" id
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Delete the menu - Bad request - invalid 'id' field
    Given the application is running
    And the menu list is not empty
    When I delete the menu with "WrongIdFormat" id
    Then the returned status code is 400


  Scenario: Delete the menu - Bad request - empty 'id' field
    Given the application is running
    And the menu list is not empty
    When I delete the menu with " " id
    Then the returned status code is 405
