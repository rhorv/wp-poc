package funding.service.application;

import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import funding.domain.command.AddPaymentToFundingBalanceCommand;
import funding.domain.event.PaymentClearedEvent;

public class PaymentClearedEventHandler implements IHandle {

  private Buffer publisher;

  public PaymentClearedEventHandler(Buffer publisher) {
    this.publisher = publisher;
  }

  public void handle(IMessage message) throws Exception {
    PaymentClearedEvent event = PaymentClearedEvent.fromMessage(message);
    publisher.publish(new AddPaymentToFundingBalanceCommand(
        event.getPaymentId(),
        event.getMerchantId(),
        event.getValue()
    ));
    publisher.flush();
  }
}