Feature: In order to be able to construct a bill for merchants, all charges of every payment
  should be calculated beforehand based on the correct tariff of the merchant

  Scenario: Calculate charges for a Blended tariff merchant
    Given I have an active merchant on a blended tariff with a 1 percent merchant charge and a fixed 0.5 GBP costs charge
    When I calculate the charges for that merchant for a payment with a value of 500 GBP and Scheme fee of 0.1 GBP and an interchange cost of 0.2 GBP
    Then That Payment will have its charges calculated at a total of 5.5 GBP

  Scenario: Calculate charges for a PassThrough tariff merchant
    Given I have an active merchant on a passthrough tariff with a 1 percent merchant charge
    When I calculate the charges for that merchant for a payment with a value of 500 GBP and Scheme fee of 0.1 GBP and an interchange cost of 0.2 GBP
    Then That Payment will have its charges calculated at a total of 5.3 GBP

  Scenario: Calculate charges for a payment for an inactive merchant
    Given I have an inactive merchant
    When I calculate the charges for that merchant for a payment with a value of 500 GBP and Scheme fee of 0.1 GBP and an interchange cost of 0.2 GBP
    Then That Payment will not have its charges calculated
