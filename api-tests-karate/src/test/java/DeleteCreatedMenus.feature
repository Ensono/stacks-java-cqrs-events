@ignore
Feature: Delete menu hook

  Scenario: Delete created menu
    * replace menu_by_id_path.menu_id = __arg.menuId
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method DELETE
    Then status 200
    * print __arg.menuId.concat(' menu was deleted')
