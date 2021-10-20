@DeleteCreatedMenu
@Functional
Feature: Delete category

  Background: Create menu in the background before the scenarios
     # 1. Create a menu
    Given the application is running
    And the following menu data:
      | name                            | description    | tenantId                             | enabled |
      | Le Renoir (Automated Test Data) | French Cuisine | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201

  @Smoke
  Scenario: Delete the category - Happy path
     # 2. Create a category
    Given the following category data:
      | name     | description                                         |
      | Desserts | This compartment contain the finest French Desserts |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201
    # 3. Delete Created Category
    When I delete the created category
    Then the returned status code is 200
    And the category is successfully deleted


  Scenario: Delete the category that contains at least one item
     # 2. Create a category
    Given the following category data:
      | name     | description                                         |
      | Desserts | This compartment contain the finest French Desserts |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201
    # 3. Create item
    Given the following item data:
      | name         | description           | price | available |
      | Coconut cake | Homemade Coconut Cake | 3.5   | true      |
    When I create an item for the previous menu and category
    Then the item was successfully created
    And the returned status code is 201
   # 4. Delete the category
    When I delete the created category
    Then the returned status code is 200
    And the category is successfully deleted


  Scenario: Remove a category that does not exist
    When I delete the category with "d222f2ee-2c22-2b22-22e6-d701722f2222" id
    Then the returned status code is 404
    And the 'category does not exist' message is returned


  Scenario: Remove a category from the menu that does not exist
    Given the application is running
    When I delete the category with "d222f2ee-2c22-2b22-22e6-d701722f2222" id for "d111f1ee-1c11-1b11-11e6-d701711f1111" menu
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Remove a category with invalid id format
    When I delete the category with "InvalidFormat" id
    Then the returned status code is 400


  Scenario: Remove a category with empty 'category id' field
    When I delete the category with " " id
    Then the returned status code is 405
