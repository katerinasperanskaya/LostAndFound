Feature: Get All Found Items (Authenticated)

  Background:
    * url baseUrl
    * def loginEndpoint = '/api/auth/login'
    * def getItemsEndpoint = '/api/found-items'

    # Login to retrieve JWT token
    Given path loginEndpoint
    And request { email: 'user', password: 'user' }
    When method POST
    Then status 200
    And def token = response.token

  Scenario: Authenticated GET request to retrieve found items
    Given path getItemsEndpoint
    And header Authorization = 'Bearer ' + token
    When method GET
    Then status 200
    And match response == '#[]' || response == '#[0]'

