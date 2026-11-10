Feature: Authentication
  Scenario: Generate access token
    #Requisites
    * def username = usrnm
    * def password = pwd
    * def requestBody = read('classpath:payloads/createApplication/00creds.json')
    * headers loginHeaders


    Given url baseUrl
    And path loginPath
    And request requestBody
    When method POST
    Then status 200
    * def accessToken = response.data.access_token






