package billing.stepdefinitions.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import billing.domain.Bill;
import billing.domain.Payment;
import billing.domain.Reference;
import billing.domain.event.PaymentAddedToBillEvent;
import billing.service.DomainEvent;
import events.IMessage;
import events.publisher.IPublish;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;

public class BillingFeature {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private Bill bill;
  private UUID paymentId;
  private BillingFeature.StubPublisher publisher;

  public BillingFeature() {
    this.publisher = new BillingFeature.StubPublisher();
    this.paymentId = UUID.randomUUID();
    DomainEvent.registerPublisher(this.publisher);
  }

  @Given("I have an open bill for a merchant with no payments on it")
  public void iHaveAnOpenBillForAMerchantWithNoPaymentsOnIt() {
    this.bill = new Bill(UUID.randomUUID(), new Reference("12345678"));
  }

  @When("I add a new payment to that bill")
  public void iAddANewPaymentToThatBill() throws Exception {
    this.bill.add(new Payment(this.paymentId, new Money(1, Currency.GBP)));
  }

  @Then("That payment will be added to that bill")
  public void thatPaymentWillBeAddedToThatBill() {
    this.bill.hasNewPayment(this.paymentId);
    assertEquals(this.publisher.messages.size(), 1);
    assertEquals(this.publisher.messages.get(0).getName(), PaymentAddedToBillEvent.NAME);
    IMessage message = this.publisher.messages.get(0);
    assertEquals(message.getPayload().get("id"), this.paymentId.toString());

  }

}
