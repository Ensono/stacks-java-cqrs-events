@Functional
Feature: Create a new menu

  @DeleteCreatedMenu @Smoke
  Scenario: Create menu - Happy path
    Given the application is running
    And the following menu data:
      | name                                   | description                            | tenantId                             | enabled |
      | Cafe de Provence (Automated Test Data) | Cafe de Provence's - French Restaurant | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    When I search the created menu by id
    Then the menu should include the following data:
      | name                                   | description                            | tenantId                             | enabled |
      | Cafe de Provence (Automated Test Data) | Cafe de Provence's - French Restaurant | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |


  Scenario: Bad request for creating menu - 404 error message
    Given the application is running
    And the following menu data:
      | name                                             | description      | tenantId                             | enabled |
      | Barge East Restaurant Menu (Automated Test Data) | Greek delicacies | d211f1ee-6c54-4b01-90e6-d701748f0111 |         |
    When I create the menu
    Then the returned status code is 400


  Scenario: Bad request for creating menu - invalid tenant Id
    Given the application is running
    And the following menu data:
      | name                                              | description       | tenantId | enabled |
      | Menu with invalid tenant Id (Automated Test Data) | Invalid tenant Id | d29      | true    |
    When I create the menu
    Then the returned status code is 400


  Scenario: Bad request for creating menu - empty tenant Id
    Given the application is running
    And the following menu data:
      | name                                            | description     | tenantId | enabled |
      | Menu with empty tenant Id (Automated Test Data) | Empty tenant Id |          | true    |
    When I create the menu
    Then the returned status code is 400

  @DeleteCreatedMenu
  Scenario: Create a menu with the same data - 409
    Given the application is running
    And the following menu data:
      | name                                       | description              | tenantId                             | enabled |
      | Bel Canto Restaurant (Automated Test Data) | Vegetarian Friendly Menu | d211f1ee-6c54-4b01-90e6-d701748f0111 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    When the following menu data:
      | name                                       | description              | tenantId                             | enabled |
      | Bel Canto Restaurant (Automated Test Data) | Vegetarian Friendly Menu | d211f1ee-6c54-4b01-90e6-d701748f0111 | true    |
    And I create the menu
    Then the returned status code is 409
    And the 'menu already exists' message is returned
