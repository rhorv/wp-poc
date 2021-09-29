package onboarding.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import billing.domain.Bill;
import billing.domain.Reference;
import events.IMessage;
import events.publisher.IPublish;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import money.Currency;
import money.Money;
import onboarding.domain.event.MerchantAssignedToLcpEvent;
import onboarding.service.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MerchantTest {
  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private StubPublisher buffer;

  @BeforeEach
  public void setUp() throws Exception {
    this.buffer = new StubPublisher();
    DomainEvent.registerPublisher(this.buffer);
  }

  @Test
  void testItCanBeAssignedToAnLcp() throws Exception {
    Lcp lcp = new Lcp("test");
    UUID merchantId = UUID.randomUUID();
    Merchant merchant = new Merchant(merchantId);
    assertFalse(merchant.isMemberOfLcp(lcp.toString()));
    merchant.assignTo(lcp);
    assertTrue(merchant.isMemberOfLcp(lcp.toString()));
    assertEquals(this.buffer.messages.size(), 1);
    assertEquals(this.buffer.messages.get(0).getName(), MerchantAssignedToLcpEvent.NAME);
  }

  @Test
  void testItCannotBeReAssigned() throws Exception {
    Lcp lcp = new Lcp("test1");
    Lcp otherLcp = new Lcp("test2");
    UUID merchantId = UUID.randomUUID();
    Merchant merchant = new Merchant(merchantId);

    Field lcpField = merchant.getClass().getDeclaredField("lcp");
    lcpField.setAccessible(true);
    lcpField.set(merchant, lcp);

    assertThrows(
        RuntimeException.class,
        () -> {
          merchant.assignTo(lcp);
        });
    assertTrue(merchant.isMemberOfLcp(lcp.toString()));
    assertFalse(merchant.isMemberOfLcp(otherLcp.toString()));
  }

}
