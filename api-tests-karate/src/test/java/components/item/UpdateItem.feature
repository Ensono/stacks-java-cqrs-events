@Functional
Feature: Update Items

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


  Scenario Outline: Update Item
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |
       # Update item for category
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    And request item_body
    When method PUT
    Then status 200
       # Check the updated item
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    * def items_list = []
    * def items_list = response.categories[0].items
    * match items_list.size() == 1
    * match items_list[0].name == <name>
    * match items_list[0].description == <description>
    * match items_list[0].available == <availability>
    @Smoke
    Examples:
      | name                                                          | description      | price | availability |
      | 'Tender chicken breast with aubergine and tomatoes - Updated' | 'Chicken breast' | 10.5  | false        |

    Examples:
      | name                                              | description            | price | availability |
      | 'Italian Cuisine - Updated (Automated Test Data)' | 'Juicy chicken breast' | 8.5   | true         |
      | 'Italian Cuisine (Automated Test Data)'           | 'Italian Pasta'        | 8.5   | true         |
      | 'Italian Cuisine (Automated Test Data)'           | 'Juicy chicken breast' | 15.5  | true         |
      | 'Italian Cuisine (Automated Test Data)'           | 'Juicy chicken breast' | 8.5   | false        |


  Scenario Outline: Update Item - Invalid request - <scenario_name>
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |
       # Update item for category
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    And request item_body
    When method PUT
    Then status 400
    * match response.description contains "Invalid Request"
    Examples:
      | scenario_name            | name                                                          | description      | price | availability |
      | Empty name field         | ''                                                            | 'Chicken breast' | 10.5  | false        |
      | Empty description field  | 'Tender chicken breast with aubergine and tomatoes - Updated' | ''               | 10.5  | false        |
      | Empty price field        | 'Tender chicken breast with aubergine and tomatoes - Updated' | 'Chicken breast' | ''    | false        |
      | Empty availability field | 'Tender chicken breast with aubergine and tomatoes - Updated' | 'Chicken breast' | 10.5  | ''           |


  Scenario Outline: Update Item - Menu not found
    * set item_body
      | path        | value                   |
      | name        | 'Tender chicken breast' |
      | description | 'Chicken breast'        |
      | price       | 16.5                    |
      | available   | true                    |
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | <menuId>                  |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |
       # Update item for category
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    And request item_body
    When method PUT
    Then status 404
    * replace menu_does_not_exists
      | token     | value    |
      | <menu_id> | <menuId> |
    * match response.description contains menu_does_not_exists
    Examples:
      | menuId                                 |
      | '261aa93f-616a-4efa-8b49-cfce77b11223' |


  Scenario Outline: Update Item - Category not found
    * set item_body
      | path        | value                   |
      | name        | 'Tender chicken breast' |
      | description | 'Chicken breast'        |
      | price       | 16.5                    |
      | available   | true                    |
    * replace item_by_id_path
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>          |
      | <item_id>     | karate.get('item_id') |
       # Update item for category
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    And request item_body
    When method PUT
    Then status 404
    * replace category_does_not_exists
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>          |
    * match response.description contains category_does_not_exists
    Examples:
      | categoryId                             |
      | '261aa93f-616a-4efa-8b49-cfce77b11223' |


  Scenario Outline: Update Item - Item not found
    * set item_body
      | path        | value                   |
      | name        | 'Tender chicken breast' |
      | description | 'Chicken breast'        |
      | price       | 16.5                    |
      | available   | true                    |
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
       # Update item for category
    Given url base_url.concat(item_by_id_path)
    And header Authorization = auth.bearer_token
    And request item_body
    When method PUT
    Then status 404
    * replace item_does_not_exists
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
    * match response.description contains item_does_not_exists
    Examples:
      | itemId                                 |
      | '261aa93f-616a-4efa-8b49-cfce77b11223' |
