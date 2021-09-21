Feature: In order to be able to pay merchants the amount their customers spent with them,
  we need to keep track of payments for them

  @model
  Scenario: a new payment gets added to an open funding balance
    Given I have an open funding balance for a merchant with no payments on it
    When I add a new payment to that funding balance
    Then That payment will be added to that funding balance

  Scenario: a funding balance gets closed
    Given I have an open funding balance for a merchant with no payments on it
    When I close that funding balance
    Then That funding balance will be closed
