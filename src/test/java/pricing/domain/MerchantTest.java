package pricing.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import events.IMessage;
import events.publisher.IPublish;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pricing.domain.event.PaymentChargeCalculatedEvent;
import pricing.service.DomainEvent;

public class MerchantTest {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private StubPublisher buffer;
  private PricingStrategy strategy;
  private Merchant merchant;

  @BeforeEach
  public void setUp() throws Exception {
    this.strategy = mock(PricingStrategy.class);
    this.buffer = new StubPublisher();
    DomainEvent.registerPublisher(this.buffer);
  }

  @Test
  void testItCalculatesCharges() throws Exception {
    this.merchant = new Merchant(UUID.randomUUID(), this.strategy);
    Money charge = new Money(1, Currency.GBP);
    when(this.strategy.calculateTotalChargeFor(any()))
        .thenReturn(charge);

    this.merchant.calculateCharges(
        new Payment(UUID.randomUUID(), new Money(1, Currency.GBP), new Money(1, Currency.GBP),
            new Money(1, Currency.GBP)));

    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), PaymentChargeCalculatedEvent.NAME);
    assertEquals(this.buffer.messages.get(0).getPayload().get("chargesAmount"),
        charge.getAmount().toString());
    assertEquals(this.buffer.messages.get(0).getPayload().get("chargesCurrency"),
        charge.getCurrency().toString());

  }

}