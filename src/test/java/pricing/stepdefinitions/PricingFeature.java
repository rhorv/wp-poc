package pricing.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import pricing.domain.BlendedTariff;
import pricing.domain.InactiveMerchant;
import pricing.domain.Merchant;
import pricing.domain.PassThroughTariff;
import pricing.domain.Payment;
import pricing.domain.event.PaymentChargeCalculatedEvent;
import pricing.domain.event.PaymentChargeCalculationFailedEvent;
import pricing.service.DomainEvent;

public class PricingFeature {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private Merchant merchant;
  private StubPublisher publisher;

  public PricingFeature() {
    this.publisher = new StubPublisher();
    DomainEvent.registerPublisher(this.publisher);
  }

  @Given("I have an inactive merchant")
  public void iHaveAnInactiveMerchant() {
    this.merchant = new InactiveMerchant(UUID.randomUUID());
  }

  @Then("That Payment will not have its charges calculated")
  public void thatPaymentWillNotHaveItsChargesCalculated() {
    assertEquals(this.publisher.messages.size(), 1);
    assertEquals(this.publisher.messages.get(0).getName(),
        PaymentChargeCalculationFailedEvent.NAME);
  }


  @Given("I have an active merchant on a blended tariff with a {float} percent merchant charge and a fixed {amount} costs charge")
  public void iHaveAnActiveMerchantOnABlendedTariffWithAPercentMerchantChargeAndAFixedCostsCharge(
      float percent, Money fixedCost) {
    this.merchant = new Merchant(UUID.randomUUID(), new BlendedTariff(percent, fixedCost));
  }

  @Given("I have an active merchant on a passthrough tariff with a {float} percent merchant charge")
  public void iHaveAnActiveMerchantOnAPassThroughTariffWithAPercentMerchantCharge(
      float percent) {
    this.merchant = new Merchant(UUID.randomUUID(), new PassThroughTariff(percent));
  }

  @When("I calculate the charges for that merchant for a payment with a value of {amount} and Scheme fee of {amount} and an interchange cost of {amount}")
  public void iCalculateTheChargesForThatMerchantForAPaymentWithAValueOfAmountAndSchemeFeeOfAmountAndAnInterchangeCostOfAmount(
      Money totalValue, Money schemeFee, Money interchangeCost) throws Exception {
    this.merchant
        .calculateCharges(new Payment(UUID.randomUUID(), totalValue, interchangeCost, schemeFee));
  }

  @Then("That Payment will have its charges calculated at a total of {amount}")
  public void thatPaymentWillHaveItsChargesCalculatedAtATotalOf(Money amount) {
    assertEquals(this.publisher.messages.size(), 1);
    assertEquals(this.publisher.messages.get(0).getName(), PaymentChargeCalculatedEvent.NAME);
    assertEquals(this.publisher.messages.get(0).getPayload().get("chargesAmount"),
        amount.getAmount().toString());
    assertEquals(this.publisher.messages.get(0).getPayload().get("chargesCurrency"),
        amount.getCurrency().toString());

  }

  @ParameterType("[0-9\\.]+\\s[A-Z]{3}")
  public Money amount(String amount) throws Exception {
    String[] parts = amount.split("\\s");
    if (parts.length != 2) {
      throw new Exception();
    }
    return new Money(Math.round(Float.valueOf(parts[0]) * 100), Currency.valueOf(parts[1]));
  }

}
