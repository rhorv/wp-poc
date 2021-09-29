Feature: In order to be able to bill merchants properly, as a business we need to categorize which
  legal entity any given merchant has a contractual relationship with

  @model
  Scenario: a new merchant gets assigned to a legal counterparty
    Given I have new merchant
    And I have an lcp
    When I assign that merchant to that lcp
    Then That merchant will be assigned to that lcp

