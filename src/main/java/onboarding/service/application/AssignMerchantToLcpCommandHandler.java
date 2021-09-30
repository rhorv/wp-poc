package onboarding.service.application;

import billing.domain.Bill;
import billing.domain.BillRepository;
import billing.domain.Payment;
import billing.domain.command.AddPaymentToBillCommand;
import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import onboarding.domain.Lcp;
import onboarding.domain.Merchant;
import onboarding.domain.MerchantRepository;
import onboarding.domain.command.AssignMerchantToLcpCommand;

public class AssignMerchantToLcpCommandHandler implements IHandle {

  private Buffer publisher;
  private MerchantRepository merchantRepository;

  public AssignMerchantToLcpCommandHandler(
      Buffer publisher, MerchantRepository merchantRepository) {
    this.publisher = publisher;
    this.merchantRepository = merchantRepository;
  }

  public void handle(IMessage message) throws Exception {
    AssignMerchantToLcpCommand command = AssignMerchantToLcpCommand.fromMessage(message);

    Merchant merchant = this.merchantRepository.get(command.getMerchantId());
    merchant.assignTo(command.getLcp());

    merchantRepository.save(merchant);
    publisher.flush();
  }
}
