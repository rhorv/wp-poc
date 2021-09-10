Feature: In order to be able to make money as a business, we need to prepare bills for merchants
  that aggregates their fees and costs

  @model
  Scenario: a new payment gets added to an open bill
    Given I have an open bill for a merchant with no payments on it
    When I add a new payment to that bill
    Then That payment will be added to that bill

  Scenario: a bill gets closed
    Given I have an open bill for a merchant with no payments on it
    When I close that bill
    Then That bill will be closed
