Feature: In order to be able to pay the merchants the money the customer spent, we need to be able
  to obtain it from the schemes via clearing

  @model
  @integration
  Scenario: a payment gets cleared
    Given There is a new payment for VISA with a value of 500 GBP
    And Scheme VISA has a scheme fee of 0.1 GBP and an interchange cost of 0.2 GBP
    When I clear the payment
    Then That payment will be cleared
    And That Payment will have a value of 500 GBP
    And That Payment will have a scheme fee of 0.1 GBP
    And That Payment will have an interchange cost of 0.2 GBP
