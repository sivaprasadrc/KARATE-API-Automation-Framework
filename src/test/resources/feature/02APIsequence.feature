@parallel=false
Feature: Home Loan
  Background:
  #Dependencies
    * def auth = callonce read('classpath:auth/authentication.feature')
    * def applicationCreation = callonce read('classpath:feature/01Initialization.feature')
    * def PayloadEncryptor = Java.type('utils.PayloadEncryptor')
    * def PayloadDecryptor = Java.type('utils.PayloadDecryptor')

  #Requisites
    * def accessToken = auth.accessToken
    * def applicationID = applicationCreation.applicationID
    * headers commonHeaders
    * header access_token = accessToken

    Given url baseUrl
    And path flowPath
    And params flowParams

  Scenario: Loan Details
    * def requestBody = read('classpath:payloads/createApplication/06LoanDetails.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200


  Scenario: Loan Product
    * def requestBody = read('classpath:payloads/createApplication/08LoanProduct.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Basic Salary
    * def requestBody = read('classpath:payloads/createApplication/09BasicSalary.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Income Details
    * def requestBody = read('classpath:payloads/createApplication/10Income.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Asset Details
    * def requestBody = read('classpath:payloads/createApplication/11AssetDetails.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: HL commitments
    * def requestBody = read('classpath:payloads/createApplication/12HLcommitments.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: PL commitments
    * def requestBody = read('classpath:payloads/createApplication/13PLcommitments.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: CC commitments
    * def requestBody = read('classpath:payloads/createApplication/14CCcommitments.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: HECS
    * def requestBody = read('classpath:payloads/createApplication/15HECS.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Expense Details
    * def requestBody = read('classpath:payloads/createApplication/16Expense.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Dependant Details
    * def requestBody = read('classpath:payloads/createApplication/17Dependant.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Profession Details
    * def requestBody = read('classpath:payloads/createApplication/18Proffession.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: ID Details
    * def requestBody = read('classpath:payloads/createApplication/19VerifyID.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Tax Details
    * def requestBody = read('classpath:payloads/createApplication/20Tax.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Statement Details
    * def requestBody = read('classpath:payloads/createApplication/21Statement.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Docu Details
    * def requestBody = read('classpath:payloads/createApplication/22Docu.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200

  Scenario: Submit
    * def requestBody = read('classpath:payloads/createApplication/23Submit.json')
    * def jsonString = karate.pretty(requestBody)
    * def encryptedPayload = PayloadEncryptor.encryptor(jsonString,secretKey)
    And request encryptedPayload
    When method POST
    Then status 200