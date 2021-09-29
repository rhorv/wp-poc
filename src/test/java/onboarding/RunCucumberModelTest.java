package onboarding;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/onboarding/onboarding.feature", glue = {"onboarding.stepdefinitions.model"}, tags = "@model")
public class RunCucumberModelTest {
}
