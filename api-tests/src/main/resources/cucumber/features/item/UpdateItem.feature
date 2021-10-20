@DeleteCreatedMenu
@Functional
Feature: Update Item

  Background: Create test data
    # 1. Create a menu
    Given the application is running
    And the following menu data:
      | name                                      | description   | tenantId                             | enabled |
      | Andy's Greek Tavern (Automated Test Data) | Greek Cuisine | d333f1ee-3c56-4b01-90e6-d701748f5656 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    # 2. Create a category
    Given the following category data:
      | name  | description                                                         |
      | Mains | This compartment contains all meat, fish or another protein source. |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201
    # 3. Create an item
    Given the following item data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |
    When I create an item for the previous menu and category
    Then the item was successfully created
    And the returned status code is 201

  @Smoke
  Scenario: Update the existing item - Happy path
    Given I update the item with the following data:
      | name         | description                                                 | price | available |
      | Updated name | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |
    Then the returned status code is 200
    When I search the updated menu
    Then the item should include the following data:
      | name         | description                                                 | price | available |
      | Updated name | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |


  Scenario: Update the existing item - update description only
    Given I update the item with the following data:
      | name    | description          | price | available |
      | Bifteki | New Description Here | 11.95 | true      |
    Then the returned status code is 200
    When I search the updated menu
    Then the item should include the following data:
      | name    | description          | price | available |
      | Bifteki | New Description Here | 11.95 | true      |


  Scenario: Update the existing item - update price only
    Given I update the item with the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 15.95 | true      |
    Then the returned status code is 200
    When I search the updated menu
    Then the item should include the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 15.95 | true      |


  Scenario: Update the existing item - update the availability only
    Given I update the item with the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | false     |
    Then the returned status code is 200
    When I search the updated menu
    Then the item should include the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | false     |


  Scenario: Update the existing item - item already exists
    Given I update the item with the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |
    Then the returned status code is 200
    When I search the updated menu
    Then the item should include the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |


  Scenario: Update the existing item with empty 'name' field
    When I update the item with the following data:
      | name | description                                                 | price | available |
      |      | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |
    Then the returned status code is 400


  Scenario: Update the existing item with empty 'description' field
    When I update the item with the following data:
      | name    | description | price | available |
      | Bifteki |             | 11.95 | true      |
    Then the returned status code is 400


  Scenario: Update the existing item with empty 'price' field
    When I update the item with the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. |       | true      |
    Then the returned status code is 400


  Scenario: Update the existing item with empty 'available' field
    When I update the item with the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 |           |
    Then the returned status code is 400


  Scenario: Update the item for the menu that does not exist
    When I update the item with the following data for the menu with "eba55cfe-5ff5-55e5-5ef5-b55b555f6e55" id:
      | name   | description         | price | available |
      | Burger | Greek-style burger. | 13.95 | true      |
    And the returned status code is 404
    Then the 'menu does not exist' message is returned


  Scenario: Update the item for the category that does not exist
    When I update the item with the following data for the category with "eba55cfe-5ff5-55e5-5ef5-b11b111f6e11" id:
      | name   | description         | price | available |
      | Burger | Greek-style burger. | 13.95 | true      |
    And the returned status code is 404
    Then the 'category does not exist' message is returned


  Scenario: Update the item for the menu invalid id format
    When I update the item with the following data for the menu with "InvalidId" id:
      | name   | description         | price | available |
      | Burger | Greek-style burger. | 13.95 | true      |
    And the returned status code is 400


  Scenario: Update the item for the category with invalid id format
    When I update the item with the following data for the category with "InvalidId" id:
      | name   | description         | price | available |
      | Burger | Greek-style burger. | 13.95 | true      |
    And the returned status code is 400
