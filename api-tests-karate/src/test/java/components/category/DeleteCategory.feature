@Functional
Feature: Delete category

  Background: Create a menu and category
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

     # create category
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
  Scenario: Delete created category
    Given url base_url.concat(category_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 200
    # Check the deleted category
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    * match response.categories.size() == 0


  Scenario Outline: Remove a category - Bad request
    Given url base_url.concat(category).concat('/').concat(<categoryId>)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 404
    * replace category_does_not_exists
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>          |
    * match response.description contains category_does_not_exists

    Examples:
      | categoryId                             |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Remove a category from the menu that does not exist
    Given url base_url.concat(menu).concat('/').concat(<id>).concat('/category/').concat(karate.get('category_id'))
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 404
    * replace menu_does_not_exists
      | token     | value |
      | <menu_id> | <id>  |
    * match response.description contains menu_does_not_exists

    Examples:
      | id                                     |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |
