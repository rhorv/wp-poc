package billing;

import clearing.domain.Scheme;
import io.cucumber.java.ParameterType;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import money.Currency;
import money.Money;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/billing/billing.feature", glue = {"billing.stepdefinitions.model"}, tags = "@model")
public class RunCucumberModelTest {
}
