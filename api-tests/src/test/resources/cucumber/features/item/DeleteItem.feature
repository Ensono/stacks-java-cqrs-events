@DeleteCreatedMenu
@Functional
Feature: Delete Item

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
  Scenario: Delete the item - Happy path
      # 4. Delete Created Item
    When I delete the created item
    Then the returned status code is 200
    And the item is successfully deleted


  Scenario: Remove an item that does not exist
    When I delete the item with "d222f2ee-2c22-2b22-22e6-d701722f2222" id
    Then the returned status code is 404
    And the 'item does not exist' message is returned


  Scenario: Remove an item from the menu that does not exist
    Given the application is running
    When I delete the item for the menu with "d222f2ee-2c22-2b22-22e6-d701722f2222" id
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Remove an item from the category that does not exist
    Given the application is running
    When I delete the item for the category with "d222f2ee-2c22-2b22-22e6-d701722f2222" id
    Then the returned status code is 404
    And the 'category does not exist' message is returned


  Scenario: Remove an item from the menu that has invalid id format
    Given the application is running
    When I delete the item for the menu with "test" id
    Then the returned status code is 400


  Scenario: Remove an item from the category that has invalid id format
    Given the application is running
    When I delete the item for the category with "InvalidID" id
    Then the returned status code is 400


  Scenario: Remove an item with empty 'category id' field
    When I delete the item with " " id
    Then the returned status code is 405
