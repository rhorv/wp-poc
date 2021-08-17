package clearing.service.infrastructure;

import clearing.domain.Scheme;
import clearing.domain.SchemeChargesProvider;
import clearing.domain.SchemeCost;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class InMemorySchemeChargesProvider implements SchemeChargesProvider {

  public SchemeCost getForSchemeAt(Scheme scheme, DateTime date) {
    return new SchemeCost(Scheme.VISA, new Money(1, Currency.GBP), new Money(2, Currency.GBP));
  }
}
