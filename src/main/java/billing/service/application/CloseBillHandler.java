package billing.service.application;

import billing.domain.Bill;
import billing.domain.BillRepository;
import billing.domain.Payment;
import billing.domain.command.AddPaymentToBillCommand;
import billing.domain.command.CloseBillCommand;
import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;

public class CloseBillHandler implements IHandle {
  private Buffer publisher;
  private BillRepository billRepository;

  public CloseBillHandler(Buffer publisher, BillRepository billRepository) {
    this.publisher = publisher;
    this.billRepository = billRepository;
  }

  public void handle(IMessage message) throws Exception {
    CloseBillCommand command = CloseBillCommand.fromMessage(message);

    Bill bill = this.billRepository.getOpenBillFor(command.getMerchantId(), command.getReference());
    bill.close();

    billRepository.save(bill);
    publisher.flush();
  }
}
