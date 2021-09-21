package funding.service.application;

import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import funding.domain.FundingBalance;
import funding.domain.FundingBalanceRepository;
import funding.domain.Payment;
import funding.domain.command.AddPaymentToFundingBalanceCommand;

public class FundingBalanceHandler implements IHandle {

  private Buffer publisher;
  private FundingBalanceRepository fundingBalanceRepository;

  public FundingBalanceHandler(
      Buffer publisher, FundingBalanceRepository fundingBalanceRepository) {
    this.publisher = publisher;
    this.fundingBalanceRepository = fundingBalanceRepository;
  }

  public void handle(IMessage message) throws Exception {
    AddPaymentToFundingBalanceCommand command =
        AddPaymentToFundingBalanceCommand.fromMessage(message);

    FundingBalance balance =
        this.fundingBalanceRepository.getOpenFundingBalanceFor(command.getMerchantId());
    balance.add(new Payment(command.getId(), command.getCharges()));

    fundingBalanceRepository.save(balance);
    publisher.flush();
  }
}
