@Functional
Feature: Delete menu

  @Smoke
  Scenario: Delete menu
    * set menu_body
      | path        | value                                   |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'  |
      | name        | 'Vegetarian Food (Automated Test Data)' |
      | description | 'The most delicious vegetarian dishes'  |
      | enabled     | true                                    |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)
    * replace menu_by_id_path.menu_id = created_menu.id

    #   Delete the created menu
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 200
    #   Check the Deleted menu
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 404
    * replace menu_does_not_exists
      | token     | value                 |
      | <menu_id> | karate.get('menu_id') |
    * match response.description contains menu_does_not_exists
    * configure afterScenario = function(){karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})}


  Scenario Outline: Delete the menu - Resource not found
    * replace menu_by_id_path.menu_id = <menuId>
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 404
    * replace menu_does_not_exists
      | token     | value    |
      | <menu_id> | <menuId> |
    * match response.description contains menu_does_not_exists
    Examples:
      | menuId                                 |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Bad request - invalid 'id' field
    * replace menu_by_id_path.menu_id = <menuId>
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status <expected_status_code>
    Examples:
      | menuId          | expected_status_code |
      | 'WrongIdFormat' | 400                  |
      | ''              | 405                  |
