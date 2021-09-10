package billing.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import billing.domain.event.BillClosedEvent;
import billing.domain.event.PaymentAddedToBillEvent;
import billing.service.DomainEvent;
import clearing.domain.event.PaymentClearedEvent;
import events.IMessage;
import events.publisher.IPublish;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BillTest {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private BillTest.StubPublisher buffer;

  @BeforeEach
  public void setUp() throws Exception {
    this.buffer = new BillTest.StubPublisher();
    DomainEvent.registerPublisher(this.buffer);
  }

  @Test
  void testItCanAddPayment() throws Exception {
    UUID paymentId = UUID.randomUUID();
    UUID merchantId = UUID.randomUUID();
    Payment payment = new Payment(paymentId, new Money(1, Currency.GBP));

    Bill bill = new Bill(merchantId, new Reference("12345678"));
    assertFalse(bill.hasNewPayment(paymentId));
    bill.add(payment);
    assertTrue(bill.hasNewPayment(paymentId));
    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), PaymentAddedToBillEvent.NAME);
  }

  @Test
  void testItCanBeClosed() throws Exception {
    Bill bill = new Bill(UUID.randomUUID(), new Reference("12345678"));
    assertTrue(bill.isOpen());
    bill.close();
    assertFalse(bill.isOpen());
    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), BillClosedEvent.NAME);
  }

}