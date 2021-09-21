package funding.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import events.IMessage;
import events.publisher.IPublish;
import funding.domain.event.FundingBalanceClosedEvent;
import funding.domain.event.PaymentAddedToFundingBalanceEvent;
import funding.service.DomainEvent;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FundingBalanceTest {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private FundingBalanceTest.StubPublisher buffer;

  @BeforeEach
  public void setUp() throws Exception {
    this.buffer = new FundingBalanceTest.StubPublisher();
    DomainEvent.registerPublisher(this.buffer);
  }

  @Test
  void testItCanAddPayment() throws Exception {
    UUID paymentId = UUID.randomUUID();
    UUID merchantId = UUID.randomUUID();
    Payment payment = new Payment(paymentId, new Money(1, Currency.GBP));

    FundingBalance balance = new FundingBalance(merchantId, new Reference("12345678"));
    assertFalse(balance.hasNewPayment(paymentId));
    balance.add(payment);
    assertTrue(balance.hasNewPayment(paymentId));
    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), PaymentAddedToFundingBalanceEvent.NAME);
  }

  @Test
  void testItCanBeClosed() throws Exception {
    FundingBalance balance = new FundingBalance(UUID.randomUUID(), new Reference("12345678"));
    assertTrue(balance.isOpen());
    balance.close();
    assertFalse(balance.isOpen());
    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), FundingBalanceClosedEvent.NAME);
  }

}