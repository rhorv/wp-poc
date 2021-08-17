package pricing.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import money.Currency;
import money.Money;
import org.junit.jupiter.api.Test;

public class BlendedTariffTest {

  @Test
  void testItCalculatesChargesCorrectly() throws Exception {

    Money totalValue = new Money(10000, Currency.GBP);
    Money interChangeCost = new Money(10, Currency.GBP);
    Money schemeFee = new Money(20, Currency.GBP);

    Money fixedBlendedCost = new Money(50, Currency.GBP);
    float merchantPercentage = 1;

    BlendedTariff tariff = new BlendedTariff(merchantPercentage, fixedBlendedCost);
    Money charges = tariff.calculateTotalChargeFor(
        new Payment(UUID.randomUUID(), totalValue, interChangeCost, schemeFee));

    assertTrue(charges.equals(totalValue.percentageOf(merchantPercentage).add(fixedBlendedCost)));
  }
}
