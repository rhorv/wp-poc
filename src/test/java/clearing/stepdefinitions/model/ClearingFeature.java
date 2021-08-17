package clearing.stepdefinitions.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import clearing.domain.Payment;
import clearing.domain.Scheme;
import clearing.domain.SchemeChargesProvider;
import clearing.domain.SchemeCost;
import clearing.domain.event.PaymentClearedEvent;
import clearing.service.DomainEvent;
import events.IMessage;
import events.publisher.IPublish;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class ClearingFeature {

  private class FixedCostProvider implements SchemeChargesProvider {

    private SchemeCost costs;

    public FixedCostProvider(SchemeCost costs) {
      this.costs = costs;
    }

    public SchemeCost getForSchemeAt(Scheme scheme, DateTime date) {
      return this.costs;
    }
  }

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }


  private Payment payment;
  private SchemeCost schemeCost;
  private StubPublisher publisher;

  public ClearingFeature() {
    this.publisher = new StubPublisher();
    DomainEvent.registerPublisher(this.publisher);
  }

  @Given("There is a new payment for {scheme} with a value of {amount}")
  public void thereIsANewPaymentForSchemeWithAValueOfAmount(Scheme scheme, Money amount) {
    this.payment = new Payment(scheme, amount, DateTime.now(), UUID.randomUUID());
  }

  @And("Scheme {scheme} has a scheme fee of {amount} and an interchange cost of {amount}")
  public void schemeHasASchemeFeeOfAmountAndAnInterchangeCostOfAmount(Scheme scheme, Money schemeFee, Money interchange) {
    this.schemeCost = new SchemeCost(scheme, interchange, schemeFee);
  }

  @When("I clear the payment")
  public void iClearThePayment() throws Exception {
    this.payment.clear(new FixedCostProvider(this.schemeCost));
  }

  @Then("That payment will be cleared")
  public void thatPaymentWillBeCleared() {
    assertEquals(this.publisher.messages.size(), 1);
    assertEquals(this.publisher.messages.get(0).getName(), PaymentClearedEvent.NAME);
  }

  @And("That Payment will have a value of {amount}")
  public void thatPaymentWillHaveAValueOfAmount(Money amount) {
    IMessage message = this.publisher.messages.get(0);
    assertEquals(Integer.valueOf(message.getPayload().get("value")), amount.getAmount());
    assertEquals(message.getPayload().get("currency"), amount.getCurrency().toString());
  }

  @And("That Payment will have a scheme fee of {amount}")
  public void thatPaymentWillHaveASchemeFeeOfAmount(Money amount) {
    IMessage message = this.publisher.messages.get(0);
    assertEquals(Integer.valueOf(message.getPayload().get("schemeFeeAmount")), amount.getAmount());
    assertEquals(message.getPayload().get("schemeFeeCurrency"), amount.getCurrency().toString());
  }

  @And("That Payment will have an interchange cost of {amount}")
  public void thatPaymentWillHaveAnInterchangeCostOfAmount(Money amount) {
    IMessage message = this.publisher.messages.get(0);
    assertEquals(Integer.valueOf(message.getPayload().get("interchangeCostAmount")), amount.getAmount());
    assertEquals(message.getPayload().get("interchangeCostCurrency"), amount.getCurrency().toString());
  }

  @ParameterType("[0-9\\.]+\\s[A-Z]{3}")
  public Money amount(String amount) throws Exception {
    String[] parts = amount.split("\\s");
    if (parts.length != 2) {
      throw new Exception();
    }
    return new Money(Math.round(Float.valueOf(parts[0]) * 100), Currency.valueOf(parts[1]));
  }

  @ParameterType("VISA")
  public Scheme scheme(String scheme) {
    return Scheme.valueOf(scheme);
  }

}
