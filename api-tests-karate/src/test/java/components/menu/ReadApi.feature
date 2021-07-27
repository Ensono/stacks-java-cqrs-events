@Functional
Feature: Read API

  Scenario: Check Application Status
    Given url base_url.concat(status)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200

  Scenario: Get all the menus
    Given url base_url.concat(menu)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200

  Scenario Outline: Search menu by id - id not found
    * replace menu_by_id_path.menu_id = <id>
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 404
    And assert response.description == "A menu with id <id> does not exist."

    Examples:
      | id                                     |
      | 'f91d2f8c-35cc-45dd-11b1-11ca548e1111' |

  Scenario: Search menu by page size and page number
    Given url base_url.concat(menu)
    And header Authorization = auth.bearer_token
    And param pageSize = 1
    And param pageNumber = 2
    When method GET
    Then status 200
