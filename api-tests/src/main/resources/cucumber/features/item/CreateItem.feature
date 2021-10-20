@DeleteCreatedMenu
@Functional
Feature: Add menu item

  Background: Create menu and category before the scenarios
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

  @Smoke
  Scenario: Create an item for the menu
    Given the following item data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |
    When I create an item for the previous menu and category
    Then the item was successfully created
    And the returned status code is 201
    And the created item should include the following data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 11.95 | true      |


  Scenario: Create an item - 409 Code the item already exists
    Given the following item data:
      | name    | description                                     | price | available |
      | Stifado | Beef stew cooked with onions for extra flavour. | 12.95 | true      |
    When I create an item for the previous menu and category
    Then the returned status code is 201
#    Create the same item twice
    Given the following item data:
      | name    | description                                     | price | available |
      | Stifado | Beef stew cooked with onions for extra flavour. | 12.95 | true      |
    When I create an item for the previous menu and category
    Then the returned status code is 409
    And the 'item already exists' message is returned


  Scenario: Create an item - 400 Bad request for invalid 'category id' field
    Given the following item data:
      | name           | description           | price | available |
      | Test Item Name | Test Item Description | 1.5   | true      |
    When I create an item for the category with "InvalidCategoryField" id
    Then the returned status code is 400


  Scenario: Create an item - 400 Bad request for invalid 'menu id' field
    Given the following item data:
      | name      | description | price | available |
      | Item Name | Item Name   | 1.5   | true      |
    When I create an item for the menu with "InvalidMenuID" id
    Then the returned status code is 400


  Scenario: Create an item - 404 Code - the menu does not exist
    Given the following item data:
      | name      | description      | price | available |
      | Item Name | Item Description | 1.5   | true      |
    When I create an item for the menu with "f11d1f1c-11cc-11dd-11b0-86ca548e0119" id
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Create an item - 404 Code - the category does not exist
    Given the following item data:
      | name      | description      | price | available |
      | Item Name | Item Description | 1.5   | true      |
    When I create an item for the category with "f00d1f1c-00cc-00dd-11b0-86ca548e0000" id
    Then the returned status code is 404
    Then the 'category does not exist' message is returned


  Scenario: Create an item for the menu - empty name
    Given the following item data:
      | name | description                                                 | price | available |
      |      | Greek-style burger patties served with cheese and tomatoes. | 10.5  | true      |
    When I create an item for the previous menu and category
    And the returned status code is 400

  Scenario: Create an item for the menu - empty description
    Given the following item data:
      | name    | description | price | available |
      | Bifteki |             | 10.5  | true      |
    When I create an item for the previous menu and category
    And the returned status code is 400

  Scenario: Create an item for the menu - empty name
    Given the following item data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. |       | true      |
    When I create an item for the previous menu and category
    And the returned status code is 400

  Scenario: Create an item for the menu - empty name
    Given the following item data:
      | name    | description                                                 | price | available |
      | Bifteki | Greek-style burger patties served with cheese and tomatoes. | 10.5  |           |
    When I create an item for the previous menu and category
    And the returned status code is 400
