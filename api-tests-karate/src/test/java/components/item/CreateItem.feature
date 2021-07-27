@Functional
Feature: Create Items

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
       # Create category
    * replace category.menu_id = karate.get('menu_id')
    * set category_body
      | path        | value                                                       |
      | name        | 'Meat plates (Automated Test Data)'                         |
      | description | 'This category contains all possible meat cooking methods.' |
    * def created_category = karate.call(read('classpath:CreateGenericData.feature'), {body:category_body, url:base_url.concat(category)})
    * karate.set('category_id',created_category.id)
    * configure afterScenario = function(){karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})}


  @Smoke
  Scenario Outline: Create Item
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
    * def create_item_path = base_url.concat(category_by_id_path).concat(items)
       # Create item
    * def created_item = karate.call(read('classpath:CreateGenericData.feature'), {body:item_body, url:create_item_path})
    * karate.set('item_id',created_item.id)

       # Check the created item
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

    Examples:
      | name                                                | description            | price | availability |
      | 'Tender chicken breast with aubergine and tomatoes' | 'Juicy chicken breast' | 8.5   | true         |


  Scenario Outline: Create an item - 409 Code the item already exists
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
    * def create_item_path = base_url.concat(category_by_id_path).concat(items)
       # Create item for category
    * def created_item = karate.call(read('classpath:CreateGenericData.feature'), {body:item_body, url:create_item_path})
       # Create the same item twice
    Given url create_item_path
    And header Authorization = auth.bearer_token
    And request item_body
    When method POST
    Then status 409
    * match response.description contains "An item with the name <name> already exists"

    Examples:
      | name                                                | description            | price | availability |
      | 'Tender chicken breast with aubergine and tomatoes' | 'Juicy chicken breast' | 8.5   | true         |


  Scenario Outline: Create an item - 400 Bad request for invalid '<field_name>' field
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |

     # Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And header Authorization = auth.bearer_token
    And request item_body
    When method POST
    Then status 400
    And match response.description contains "Invalid Request"

    Examples:
      | field_name   | name             | description            | price | availability | categoryId                             |
      | name         | ''               | 'Juicy chicken breast' | 8.5   | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |
      | description  | 'Tender chicken' | ''                     | 8.5   | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |
      | price        | 'Tender chicken' | 'Juicy chicken breast' |       | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |
      | availability | 'Tender chicken' | 'Juicy chicken breast' | 8.5   |              | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |


  Scenario Outline: Create an item - 404 Code - the menu does not exist
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | <id>                      |
      | <category_id> | karate.get('category_id') |

#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And header Authorization = auth.bearer_token
    And request item_body
    When method POST
    Then status 404
    * match response.description contains "A menu with id <id> does not exist."

    Examples:
      | name             | description            | price | availability | id                                     |
      | 'Tender chicken' | 'Juicy chicken breast' | 8.5   | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |


  Scenario Outline: Scenario: Create an item - 404 Code - the category does not exist
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <id>                  |
#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And header Authorization = auth.bearer_token
    And request item_body
    When method POST
    Then status 404
    * match response.description contains "A category with the id <id> does not exist"

    Examples:
      | name             | description            | price | availability | id                                     |
      | 'Tender chicken' | 'Juicy chicken breast' | 8.5   | true         | 'c1111d5b-314e-4a37-bb4b-526aff00cb09' |
