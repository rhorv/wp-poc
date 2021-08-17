package clearing;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/clearing/clearing.feature", glue = {"clearing.stepdefinitions.integration"}, tags = "@integration")
public class RunCucumberIntegrationTest {
}
