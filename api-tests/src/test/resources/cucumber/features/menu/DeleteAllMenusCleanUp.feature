@DeleteAllMenusCleanUp
Feature: This test will delete all existing menus from the environment

#  This scenario will delete all existing menus.
  @Ignore
  Scenario: Delete all existing menus from the environment
    Given the application is running
    And the menu list is not empty
    When I delete all existing menus
    Then the menu list is empty


#  This scenario will delete all menus created by '(Automated Test Data)'
  @Ignore
  Scenario: Delete all existing menus from the environment
    Given the application is running
    And the menu list is not empty
    When I delete all menus from previous tests
    Then the menu list is empty
