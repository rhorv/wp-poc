package clearing.domain;

import org.joda.time.DateTime;

public interface SchemeChargesProvider {

  public SchemeCost getForSchemeAt(Scheme scheme, DateTime date);
}
