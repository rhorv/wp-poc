package clearing.service.application;

import clearing.domain.Payment;
import clearing.domain.PaymentRepository;
import clearing.domain.SchemeChargesProvider;
import clearing.domain.command.ClearPaymentCommand;
import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;

public class ClearPaymentCommandHandler implements IHandle {

  private PaymentRepository paymentRepository;
  private SchemeChargesProvider schemeChargesProvider;
  private Buffer publisher;

  public ClearPaymentCommandHandler(PaymentRepository paymentRepository,
      SchemeChargesProvider schemeChargesProvider, Buffer publisher) {
    this.paymentRepository = paymentRepository;
    this.schemeChargesProvider = schemeChargesProvider;
    this.publisher = publisher;
  }

  public void handle(IMessage message) throws Exception {
    ClearPaymentCommand clearPaymentCommand = ClearPaymentCommand.fromMessage(message);

    Payment payment = this.paymentRepository.get(clearPaymentCommand.getPaymentId());
    payment.clear(this.schemeChargesProvider);

    paymentRepository.save(payment);
    publisher.flush();
  }
}
