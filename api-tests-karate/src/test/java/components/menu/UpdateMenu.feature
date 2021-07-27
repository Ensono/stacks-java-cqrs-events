@Functional
Feature: Update a menu

  Background: Create menu and Category for future items
     # Create a menu
    * set menu_body
      | path        | value                                   |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'  |
      | name        | 'Italian Cuisine (Automated Test Data)' |
      | description | 'The most delicious Italian dishes'     |
      | enabled     | true                                    |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)
    * configure afterScenario = function(){karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})}

  @Smoke
  Scenario Outline: Update menu
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method PUT
    Then status 200
     # Check the updated menu
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    And response.name == "<name>"
    And response.description == "<description>"
    And response.tenantId == "<tenantId>"
    And response.enabled == <enabled>

    Examples:
      | tenantId                               | name                                      | description                            | enabled |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Updated Menu Name (Automated Test Data)' | 'The most delicious vegetarian dishes' | true    |


  Scenario Outline: Update menu - empty mandatory fields
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method PUT
    Then status 400
    * match response.description contains "Invalid Request"

    Examples:
      | tenantId                               | name                                      | description                            | enabled |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | ''                                        | 'The most delicious vegetarian dishes' | true    |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Updated Menu Name (Automated Test Data)' | ''                                     | true    |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Updated Menu Name (Automated Test Data)' | 'The most delicious vegetarian dishes' | ''      |

