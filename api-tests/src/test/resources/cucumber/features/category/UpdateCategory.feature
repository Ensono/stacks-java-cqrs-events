@DeleteCreatedMenu
@Functional
Feature: Update categories

  Background: Create menu and the category in the background before updating them
     # 1. Create a menu
    Given the application is running
    And the following menu data:
      | name                            | description    | tenantId                             | enabled |
      | Le Renoir (Automated Test Data) | French Cuisine | d211f1ee-6c56-4b01-90e6-d701748f5656 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    # 1. Create a category
    Given the following category data:
      | name          | description                                   |
      | Fish Category | This compartment contains all fish delicacies |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201

  @Smoke
  Scenario: Update the category - Happy Path
    When I update the category with the following data:
      | name     | description                                          |
      | Desserts | This compartment contains the finest French Desserts |
    Then the returned status code is 200
    When I search the updated menu
    Then the menu should include the following category data:
      | name     | description                                          |
      | Desserts | This compartment contains the finest French Desserts |


  Scenario: Update category 'name' field only
    When I update the category with the following data:
      | name                  | description                                   |
      | Updated Category Name | This compartment contains all fish delicacies |
    Then the returned status code is 200
    When I search the updated menu
    Then the menu should include the following category data:
      | name                  | description                                   |
      | Updated Category Name | This compartment contains all fish delicacies |


  Scenario: Update category 'description' field is empty
    When I update the category with the following data:
      | name                  | description |
      | Updated Category Name |             |
    Then the returned status code is 400


  Scenario: Update category 'name' field is empty
    When I update the category with the following data:
      | name | description                  |
      |      | Updated Category Description |
    Then the returned status code is 400


  Scenario: Update category 'name' field is empty
    When I update the category with the following data:
      | name | description |
      |      |             |
    Then the returned status code is 400


  Scenario: Update category 'description' field only
    When I update the category with the following data:
      | name          | description                                             |
      | Fish Category | This compartment contains all fish delicacies - Updated |
    Then the returned status code is 200
    When I search the updated menu
    Then the menu should include the following category data:
      | name          | description                                             |
      | Fish Category | This compartment contains all fish delicacies - Updated |


  Scenario: Update the category for the menu that does not exist
    When I update the category for the menu with "eba55cfe-5ff5-55e5-5ef5-b55b555f6e55" id with the following data:
      | name     | description                                          |
      | Desserts | This compartment contains the finest French Desserts |
    And the returned status code is 404
    Then the 'menu does not exist' message is returned


  Scenario: Update the category for category id that does not exist
    When I update the category with "abc11cfe-1ff1-11e1-1ef1-b11b111f6e11" id with the following data:
      | name     | description                                          |
      | Desserts | This compartment contains the finest French Desserts |
    And the returned status code is 404
    Then the 'category does not exist' message is returned


  Scenario: Update the category - invalid category id
    When I update the category with "abc" id with the following data:
      | name     | description                                          |
      | Desserts | This compartment contains the finest French Desserts |
    And the returned status code is 400
