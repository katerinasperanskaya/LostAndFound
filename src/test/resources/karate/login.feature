Feature: Login functionality

  Background:
    * url baseUrl
    * def loginEndpoint = '/api/auth/login'

  Scenario: Valid user login
    Given path loginEndpoint
    And request { email: 'user', password: 'user' }
    When method POST
    Then status 200
    And match response.message == 'Login successful'
    And def token = response.token

  Scenario: Valid admin login
    Given path loginEndpoint
    And request { email: 'admin', password: 'admin' }
    When method POST
    Then status 200
    And match response.message == 'Login successful'

  Scenario: Invalid user email
    Given path loginEndpoint
    And request { email: 'wrong', password: 'user' }
    When method POST
    Then status 404
    And match response.message == 'User not found'

  Scenario: Invalid password
    Given path loginEndpoint
    And request { email: 'user', password: 'wrong' }
    When method POST
    Then status 401
    And match response.message == 'Invalid credentials'
