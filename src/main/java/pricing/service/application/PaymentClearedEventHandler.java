package pricing.service.application;

import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import events.publisher.IPublish;
import pricing.domain.command.CalculateChargesCommand;
import pricing.domain.event.PaymentClearedEvent;

public class PaymentClearedEventHandler implements IHandle {

  private Buffer publisher;

  public PaymentClearedEventHandler(Buffer publisher) {
    this.publisher = publisher;
  }

  public void handle(IMessage message) throws Exception {
    PaymentClearedEvent paymentClearedEvent = PaymentClearedEvent.fromMessage(message);
    publisher.publish(new CalculateChargesCommand(
        paymentClearedEvent.getPaymentId(),
        paymentClearedEvent.getValue(),
        paymentClearedEvent.getInterchangeCost(),
        paymentClearedEvent.getSchemeFee(),
        paymentClearedEvent.getMerchantId()
    ));
    publisher.flush();
  }
}
