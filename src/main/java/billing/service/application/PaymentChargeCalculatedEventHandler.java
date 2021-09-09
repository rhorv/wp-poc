package billing.service.application;

import billing.domain.command.AddPaymentToBillCommand;
import billing.domain.event.PaymentChargeCalculatedEvent;
import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;

public class PaymentChargeCalculatedEventHandler implements IHandle {

  private Buffer publisher;

  public PaymentChargeCalculatedEventHandler(Buffer publisher) {
    this.publisher = publisher;
  }

  public void handle(IMessage message) throws Exception {
    PaymentChargeCalculatedEvent event = PaymentChargeCalculatedEvent.fromMessage(message);
    publisher.publish(new AddPaymentToBillCommand(
        event.getPaymentId(),
        event.getMerchantId(),
        event.getCharges()
    ));
    publisher.flush();
  }
}