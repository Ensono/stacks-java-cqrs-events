Feature: Check on application status
  The `/health` endpoint returns a status message to indicate that the application is running successfully.

  @Smoke @Functional
  Scenario: Application status end-point
    Given the application is running
    When I check the application status
    Then the API should return "UP" status
