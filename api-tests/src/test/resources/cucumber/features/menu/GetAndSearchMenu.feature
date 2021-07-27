@Functional
Feature: Search menu
  Description - These are the scenarios related to getting menu read API

  @DeleteCreatedMenu @Smoke
  Scenario: Get menu by id
    Given the application is running
    And the following menu data:
      | name                            | description    | tenantId                             | enabled |
      | Le Renoir (Automated Test Data) | French Cuisine | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    And I search the created menu by id
    Then the returned status code is 200
    And the menu should include the following data:
      | name                            | description    | tenantId                             | enabled |
      | Le Renoir (Automated Test Data) | French Cuisine | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |


  @DeleteCreatedMenu
  Scenario: Search menu by search term
    Given the application is running
    And the following menu data:
      | name                             | description                           | tenantId                             | enabled |
      | Lunch Menu (Automated Test Data) | A delicious food selection for lunch. | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    And the menu list is not empty
    When I search the menu by criteria
      | searchTerm | Lunch Menu |
    Then the returned status code is 200


  Scenario: Search menu by invalid id
    Given the application is running
    When I search the menu by:
      | id            |
      | InvalidMenuID |
    Then the returned status code is 400


  Scenario: Search menu with id that does not exist
    Given the application is running
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-11b1-11ca548e1111 |
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Search menu by incorrect 'id' format
    Given the application is running
    When I search the menu by:
      | id  |
      | abc |
    Then the returned status code is 400


  @DeleteCreatedMenu @Smoke
  Scenario: Get menu by id - v2
    Given the application is running
    And the following menu data:
      | name                               | description                 | tenantId                             | enabled |
      | Andy's Pizza (Automated Test Data) | The best pizza in your town | d211f1ee-6c54-4b01-90e6-d755748f0852 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    And I search the created menu by id in the v2 app version
    Then the returned status code is 200
    And the menu should include the following data:
      | name                               | description                 | tenantId                             | enabled |
      | Andy's Pizza (Automated Test Data) | The best pizza in your town | d211f1ee-6c54-4b01-90e6-d755748f0852 | true    |


  Scenario: Search menu by invalid id - v2
    Given the application is running
    When in the v2 app version I search the menu by:
      | id            |
      | InvalidMenuID |
    Then the returned status code is 400


  Scenario: Search menu with id that does not exist - v2
    Given the application is running
    When in the v2 app version I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-11b1-11ca548e1111 |
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Search menu by incorrect 'id' format - v2
    Given the application is running
    When in the v2 app version I search the menu by:
      | id  |
      | abc |
    Then the returned status code is 400


  Scenario: Search menu by invalid name
    Given the application is running
    When I search the menu by:
      | name        |
      | InvalidName |
    Then the returned status code is 400


  @DeleteCreatedMenu
  Scenario: Search menu by restaurant Id
    Given the application is running
    And the following menu data:
      | name                             | description                           | tenantId                             | enabled |
      | Lunch Menu (Automated Test Data) | A delicious food selection for lunch. | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201

    When I search the menu by criteria
      | restaurantId | d211f1ee-6c54-4b01-90e6-d701748f0852 |
    Then the returned status code is 200
    And the menu should include the following details:
      | name                             | description                           | tenantId                             | enabled |
      | Lunch Menu (Automated Test Data) | A delicious food selection for lunch. | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |

  @DeleteCreatedMenu
  Scenario: Get menus by page size
    Given the application is running
    And the following menu data:
      | name                             | description                           | tenantId                             | enabled |
      | Lunch Menu (Automated Test Data) | A delicious food selection for lunch. | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |
    When I create the menu
    Then the menu was successfully created
    And the menu list is not empty
    When I search the menu by criteria
      | pageSize | 1 |
    And the returned status code is 200
    Then the 1 items are returned


  Scenario: Get menu by page size - invalid page size input
    Given the application is running
    When I search the menu by criteria
      | pageSize | abc |
    Then the returned status code is 400


  Scenario: Search menu by multiple criteria
    Given the application is running
    When I search the menu by criteria
      | pageSize   | 1 |
      | pageNumber | 2 |
    Then the returned status code is 200
