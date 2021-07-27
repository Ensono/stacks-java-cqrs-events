@Functional
Feature: Create a menu

  @Smoke
  Scenario Outline: Create menu
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)
    * replace menu_by_id_path.menu_id = created_menu.id
#    Check the created menu
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    And response.name == "<name>"
    And response.description == "<description>"
    And response.tenantId == "<tenantId>"
    And response.enabled == <enabled>
    * karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})

    Examples:
      | tenantId                               | name                                    | description                            | enabled |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Vegetarian Food (Automated Test Data)' | 'The most delicious vegetarian dishes' | true    |


  Scenario Outline: Bad request for creating menu - <scenario_name>
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    Given url base_url.concat(menu)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method POST
    Then status 400

    Examples:
      | scenario_name             | tenantId                               | name                                     | description         | enabled |
      | empty 'enabled' field     | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Cafe de Provence (Automated Test Data)' | 'French Restaurant' |         |
      | empty 'description' field | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Cafe de Provence (Automated Test Data)' | ''                  | true    |
      | empty 'name' field        | '74b858a4-d00f-11ea-87d0-0242ac130003' | ''                                       | 'French Restaurant' | true    |
      | empty 'tenantId' field    | ''                                     | 'Cafe de Provence (Automated Test Data)' | 'French Restaurant' | true    |


  Scenario Outline: Create a menu with the same data - 409
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)
    # Check the created menu
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    And response.name == "<name>"
    And response.description == "<description>"
    And response.tenantId == "<tenantId>"
    And response.enabled == <enabled>
    And request menu_body

#    Create the second menu with the same data
    Given url base_url.concat(menu)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method POST
    Then status 409
    * match response.description contains "A Menu with the name <name> already exists for the restaurant with id <tenantId>"
    * karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})

    Examples:
      | tenantId                               | name                                         | description                | enabled |
      | 'd211f1ee-6c54-4b01-90e6-d701748f0111' | 'Bel Canto Restaurant (Automated Test Data)' | 'Vegetarian Friendly Menu' | true    |
