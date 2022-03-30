package settlement;

import events.consumer.IConsume;
import events.dispatcher.IDispatch;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {

  public void start() {

    ApplicationContext appContext =
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    IConsume consumer = (IConsume) appContext.getBean("settlement.messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("settlement.bufferPublisher");

    settlement.service.DomainEvent.registerPublisher(bufferPublisher);

    dispatcher.subscribe(
        "bill_closed", (IHandle) appContext.getBean("BillClosedEventHandler"));
    dispatcher.subscribe(
        "add_bill_to_settlement", (IHandle) appContext.getBean("BillClosedEventHandler"));
    dispatcher.subscribe(
        "funding_balance_closed", (IHandle) appContext.getBean("FundingBalanceClosedEventHandler"));
    dispatcher.subscribe(
        "add_funding_balance_to_settlement",
        (IHandle) appContext.getBean("BillClosedEventHandler"));
    dispatcher.subscribe(
        "settlement_closed", (IHandle) appContext.getBean("SettlementClosedEventHandler"));
    dispatcher.subscribe(
        "send_settlement", (IHandle) appContext.getBean("SettlementUploaderHandler"));

    try {
      consumer.consume();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
