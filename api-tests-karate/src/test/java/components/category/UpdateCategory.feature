@Functional
Feature: Update a category

  Background: Create a menu and category test data
    * set menu_body
      | path        | value                                   |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'  |
      | name        | 'Italian Cuisine (Automated Test Data)' |
      | description | 'The most delicious Italian dishes'     |
      | enabled     | true                                    |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)
    * replace menu_by_id_path.menu_id = created_menu.id
    * replace category.menu_id = karate.get('menu_id')

    # Create category for menu
    * set category_body
      | path        | value                                                       |
      | name        | 'Meat plates (Automated Test Data)'                         |
      | description | 'This category contains all possible meat cooking methods.' |
    * def created_category = karate.call(read('classpath:CreateGenericData.feature'), {body:category_body, url:base_url.concat(category)})
    * karate.set('category_id',created_category.id)
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
    * configure afterScenario = function(){karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})}

  @Smoke
  Scenario Outline: Update created category
    * set category_body
      | path        | value         |
      | name        | <name>        |
      | description | <description> |
    Given url base_url.concat(category_by_id_path)
    And header Authorization = auth.bearer_token
    And request category_body
    When method PUT
    Then status 200
     # Check the updated category
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    * def category_list = []
    * def category_list = response.categories
    * match category_list.size() == 1
    * match category_list[0].name == <name>
    * match category_list[0].description == <description>

    Examples:
      | name                                        | description              |
      | 'Meat plates Updated (Automated Test Data)' | 'Meat Only- Updated.'    |
      | 'Meat plates (Automated Test Data)'         | 'Description - Updated.' |


  Scenario Outline: Update category - Menu not found
    * replace category.menu_id = <menuId>
    Given url base_url.concat(menu).concat('/').concat(<menuId>).concat('/category/').concat(karate.get('category_id'))
    And header Authorization = auth.bearer_token
    * set category_body
      | path        | value                   |
      | name        | 'Updated Category Name' |
      | description | 'Description'           |
    And request category_body
    When method PUT
    Then status 404
    * replace menu_does_not_exists
      | token     | value    |
      | <menu_id> | <menuId> |
    * match response.description contains menu_does_not_exists

    Examples:
      | menuId                                 |
      | 'a7938045-c0f8-4d03-a37d-108a21f11dd1' |


  Scenario Outline: Update category - Category not found
    Given url base_url.concat(category).concat('/').concat(<id>)
    And header Authorization = auth.bearer_token
    * set category_body
      | path        | value                   |
      | name        | 'Updated Category Name' |
      | description | 'Description'           |
    And request category_body
    When method PUT
    Then status 404
    * replace category_does_not_exists
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <id>                  |
    * match response.description contains category_does_not_exists

    Examples:
      | id                                     |
      | 'a7938045-c0f8-4d03-a37d-108a21f11dd1' |
