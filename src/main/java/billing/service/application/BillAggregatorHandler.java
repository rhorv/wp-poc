package billing.service.application;

import billing.domain.Bill;
import billing.domain.BillRepository;
import billing.domain.Payment;
import billing.domain.command.AddPaymentToBillCommand;
import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;

public class BillAggregatorHandler implements IHandle {

  private Buffer publisher;
  private BillRepository billRepository;

  public BillAggregatorHandler(Buffer publisher, BillRepository billRepository) {
    this.publisher = publisher;
    this.billRepository = billRepository;
  }

  public void handle(IMessage message) throws Exception {
    AddPaymentToBillCommand command = AddPaymentToBillCommand.fromMessage(message);

    Bill bill = this.billRepository.getOpenBillFor(command.getMerchantId());
    bill.add(new Payment(command.getId(), command.getCharges()));

    billRepository.save(bill);
    publisher.flush();
  }
}
