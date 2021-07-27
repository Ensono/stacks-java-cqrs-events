@ignore
Feature: Clean up the test data

  Scenario: Search and delete previous created test data
    Given url base_url.concat(menu)
    And header Authorization = auth.bearer_token
    And param searchTerm = '(Automated Test Data)'
    When method GET
    Then status 200
    * def menuIds = $response.results.[*].id
    * def deleteMenu = function(menuId){  karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:menuId})  }
    * eval karate.forEach(menuIds, deleteMenu)
