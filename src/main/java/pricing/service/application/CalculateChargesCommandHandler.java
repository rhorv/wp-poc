package pricing.service.application;

import events.IMessage;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import pricing.domain.Merchant;
import pricing.domain.MerchantRepository;
import pricing.domain.Payment;
import pricing.domain.command.CalculateChargesCommand;

public class CalculateChargesCommandHandler implements IHandle {

  private MerchantRepository merchantRepository;
  private Buffer publisher;

  public CalculateChargesCommandHandler(MerchantRepository merchantRepository,
      Buffer publisher) {
    this.merchantRepository = merchantRepository;
    this.publisher = publisher;
  }

  public void handle(IMessage message) throws Exception {
    CalculateChargesCommand calculateChargesCommand = CalculateChargesCommand.fromMessage(message);

    Merchant merchant = merchantRepository.get(calculateChargesCommand.getMerchantId());
    merchant.calculateCharges(new Payment(
        calculateChargesCommand.getPaymentId(),
        calculateChargesCommand.getValue(),
        calculateChargesCommand.getInterchangeCost(),
        calculateChargesCommand.getSchemeFee()
    ));

    publisher.flush();
  }
}
