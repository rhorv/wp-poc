package funding.stepdefinitions.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import events.IMessage;
import events.publisher.IPublish;
import funding.domain.FundingBalance;
import funding.domain.Payment;
import funding.domain.Reference;
import funding.domain.event.FundingBalanceClosedEvent;
import funding.domain.event.PaymentAddedToFundingBalanceEvent;
import funding.service.DomainEvent;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;

public class FundingFeature {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private FundingBalance balance;
  private UUID paymentId;
  private FundingFeature.StubPublisher publisher;

  public FundingFeature() {
    this.publisher = new FundingFeature.StubPublisher();
    this.paymentId = UUID.randomUUID();
    DomainEvent.registerPublisher(this.publisher);
  }

  @Given("I have an open funding balance for a merchant with no payments on it")
  public void iHaveAnOpenFundingBalanceForAMerchantWithNoPaymentsOnIt() {
    this.balance = new FundingBalance(UUID.randomUUID(), new Reference("12345678"));
  }

  @When("I add a new payment to that funding balance")
  public void iAddANewPaymentToThatFundingBalance() throws Exception {
    this.balance.add(new Payment(this.paymentId, new Money(1000, Currency.GBP)));
  }

  @Then("That payment will be added to that funding balance")
  public void thatPaymentWillBeAddedToThatFundingBalance() {
    this.balance.hasNewPayment(this.paymentId);
    assertEquals(this.publisher.messages.size(), 1);
    assertEquals(this.publisher.messages.get(0).getName(), PaymentAddedToFundingBalanceEvent.NAME);
    IMessage message = this.publisher.messages.get(0);
    assertEquals(message.getPayload().get("id"), this.paymentId.toString());
  }

  @When("I close that funding balance")
  public void iCloseThatFundingBalance() throws Exception {
    this.balance.close();
  }

  @Then("That funding balance will be closed")
  public void thatFundingBalanceWillBeClosed() {
    assertFalse(this.balance.isOpen());
    assertEquals(this.publisher.messages.size(), 1);
    assertEquals(this.publisher.messages.get(0).getName(), FundingBalanceClosedEvent.NAME);
  }


}
