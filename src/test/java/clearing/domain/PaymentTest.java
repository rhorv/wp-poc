package clearing.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import clearing.domain.event.PaymentClearedEvent;
import clearing.service.DomainEvent;
import events.IMessage;
import events.publisher.Buffer;
import events.publisher.IPublish;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentTest {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private SchemeChargesProvider chargesProvider;
  private StubPublisher buffer;

  @BeforeEach
  public void setUp() throws Exception {
    this.chargesProvider = mock(SchemeChargesProvider.class);
    this.buffer = new StubPublisher();
    DomainEvent.registerPublisher(this.buffer);
  }

  @Test
  void testItWillNotAllowClearingOfPaymentThatArentNew() {
    Payment payment = new Payment(Scheme.VISA, new Money(1, Currency.GBP), DateTime.now(),
        UUID.randomUUID());

    when(this.chargesProvider.getForSchemeAt(any(), any()))
        .thenReturn(
            new SchemeCost(Scheme.VISA, new Money(1, Currency.GBP), new Money(1, Currency.GBP)));

    try {
      Field statusField = payment.getClass().getDeclaredField("status");
      statusField.setAccessible(true);
      statusField.set(payment, PaymentStatus.CLEARED);
    } catch (Exception e) {
    }

    assertThrows(RuntimeException.class, () -> {
      payment.clear(this.chargesProvider);
    });
  }

  @Test
  void testItClearsNewPayment() throws Exception {
    Payment payment = new Payment(Scheme.VISA, new Money(1, Currency.GBP), DateTime.now(),
        UUID.randomUUID());

    when(this.chargesProvider.getForSchemeAt(any(), any()))
        .thenReturn(
            new SchemeCost(Scheme.VISA, new Money(1, Currency.GBP), new Money(1, Currency.GBP)));

    payment.clear(this.chargesProvider);
    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), PaymentClearedEvent.NAME);
    assertTrue(payment.isCleared());
  }

}