@Functional
Feature: Delete item

  Background: Create test data
       # Create a menu
    * set menu_body
      | path        | value                                   |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'  |
      | name        | 'Italian Cuisine (Automated Test Data)' |
      | description | 'The most delicious Italian dishes'     |
      | enabled     | true                                    |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)
       # Create category
    * replace category.menu_id = karate.get('menu_id')
    * set category_body
      | path        | value                                                       |
      | name        | 'Meat plates (Automated Test Data)'                         |
      | description | 'This category contains all possible meat cooking methods.' |
    * def created_category = karate.call(read('classpath:CreateGenericData.feature'), {body:category_body, url:base_url.concat(category)})
    * karate.set('category_id',created_category.id)
       # Create item
    * set item_body
      | path        | value                                               |
      | name        | 'Tender chicken breast with aubergine and tomatoes' |
      | description | 'Juicy chicken breast'                              |
      | price       | 8.5                                                 |
      | available   | true                                                |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
    * def create_item_path = base_url.concat(category_by_id_path).concat(items)
    * def created_item = karate.call(read('classpath:CreateGenericData.feature'), {body:item_body, url:create_item_path})
    * karate.set('item_id',created_item.id)
    * configure afterScenario = function(){karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})}


  @Smoke
  Scenario: Delete created item
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |
    # Delete the created item
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 200
    # Check the Deleted item
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    * def categories_list = response.categories
    * match categories_list[0].items.size() == 0


  Scenario Outline: Remove an item that does not exist
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
    # Delete the item
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 404
    * replace item_does_not_exists
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
    * match response.description contains item_does_not_exists
    Examples:
      | itemId                                 |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Remove an item for a category that does not exist
    * replace item_by_id_path
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>          |
      | <item_id>     | karate.get('item_id') |
    # Delete the created item
    Given url base_url.concat(item_by_id_path)
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


  Scenario Outline: Remove an item for a menu that does not exist
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | <menuId>                  |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |
    # Delete the item
    Given url base_url.concat(item_by_id_path)
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


  Scenario Outline: Bad Request - invalid item id
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
    # Delete the item
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status <status_code>

    Examples:
      | itemId            | status_code |
      | 'InvalidIdFormat' | 400         |
      | ''                | 405         |
