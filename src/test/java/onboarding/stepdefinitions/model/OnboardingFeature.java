package onboarding.stepdefinitions.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import events.IMessage;
import events.publisher.IPublish;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.UUID;
import onboarding.domain.Lcp;
import onboarding.domain.Merchant;
import onboarding.domain.event.MerchantAssignedToLcpEvent;
import onboarding.service.DomainEvent;

public class OnboardingFeature {

  private class StubPublisher implements IPublish {

    public ArrayList<IMessage> messages = new ArrayList<>();

    public void publish(IMessage message) throws Exception {
      this.messages.add(message);
    }
  }

  private Merchant merchant;
  private Lcp lcp;
  private StubPublisher publisher;

  public OnboardingFeature() {
    this.publisher = new OnboardingFeature.StubPublisher();
    DomainEvent.registerPublisher(this.publisher);
  }

  @Given("I have new merchant")
  public void iHaveNewMerchant() {
    this.merchant = new Merchant(UUID.randomUUID());
  }

  @And("I have an lcp")
  public void iHaveAnLcp() {
    this.lcp = new Lcp("test");
  }

  @When("I assign that merchant to that lcp")
  public void iAssignThatMerchantToThatLcp() throws Exception {
    this.merchant.assignTo(this.lcp);
  }

  @Then("That merchant will be assigned to that lcp")
  public void thatMerchantWillBeAssignedToThatLcp() {
    assertTrue(this.merchant.isMemberOfLcp(this.lcp.toString()));
    assertEquals(this.publisher.messages.get(0).getName(), MerchantAssignedToLcpEvent.NAME);
    IMessage message = this.publisher.messages.get(0);
    assertEquals(message.getPayload().get("id"), this.merchant.getId().toString());
    assertEquals(message.getPayload().get("lcp"), this.lcp.toString());
  }
}
