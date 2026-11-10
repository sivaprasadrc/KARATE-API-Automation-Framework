@parallel=false
Feature: Consumer FLow
Background:
 #Dependencies
  * def auth = callonce read('classpath:auth/authentication.feature')
  * def PayloadEncryptor = Java.type('utils.PayloadEncryptor')
  * def PayloadDecryptor = Java.type('utils.PayloadDecryptor')
 #Requisites
  * def accessToken = auth.accessToken
  * headers commonHeaders
  * header access_token = accessToken

  Given url baseUrl
  And path flowPath
  And params flowParams


  Scenario: Welcome Page


    * def requestBody = read('classpath:payloads/createApplication/01welcomePage.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Applicant Type


    * def requestBody = read('classpath:payloads/createApplication/02applicantType.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Acknowledgement


    * def requestBody = read('classpath:payloads/createApplication/03acknowledgement.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Create Application


    * def requestBody = read('classpath:payloads/createApplication/04createApplication.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

    * def encryptedResponse = response.trim().replace(/^"|"$/g, '')
    * def jsonString  = PayloadDecryptor.decryptor(encryptedResponse, secretKey)
    * def decryptedResponse = karate.fromString(jsonString)
    * def applicationID =  decryptedResponse.data['application_id']
