package events.dispatcher.guava;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import events.IMessage;
import events.Message;
import events.dispatcher.IHandle;
import java.util.HashMap;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class GuavaDispatcherTest {

  private GuavaDispatcher dispatcher;
  private IHandle handler;

  @BeforeEach
  public void setUp() throws Exception {
    this.dispatcher = new GuavaDispatcher();
    this.handler = mock(IHandle.class);
  }

  @Test
  void testItDispatchesMessagesToSubscribedHandler() throws Exception {
    IMessage message =
        new Message(
            "event_name",
            new HashMap<String, String>(),
            1,
            new DateTime("2020-09-15T15:53:00+01:00"),
            "event");
    this.dispatcher.subscribe("event_name", this.handler);
    this.dispatcher.dispatch(message);
    ArgumentCaptor<IMessage> argument = ArgumentCaptor.forClass(IMessage.class);
    verify(this.handler, Mockito.times(1)).handle(argument.capture());
    assertEquals(argument.getValue().getName(), message.getName());
    assertEquals(argument.getValue().getVersion(), message.getVersion());
    assertEquals(argument.getValue().getPayload(), message.getPayload());
    assertEquals(argument.getValue().getOccurredAt(), message.getOccurredAt());
    assertEquals(argument.getValue().getCategory(), message.getCategory());
  }

  @Test
  void testItAllowsHandlerToSubscribeForAMessageByName() throws Exception {
    IMessage message =
        new Message(
            "event_name",
            new HashMap<String, String>(),
            1,
            new DateTime("2020-09-15T15:53:00+01:00"),
            "event");
    this.dispatcher.dispatch(message);
    ArgumentCaptor<IMessage> argument = ArgumentCaptor.forClass(IMessage.class);
    verify(this.handler, Mockito.times(0)).handle(argument.capture());

    this.dispatcher.subscribe("event_name", this.handler);
    this.dispatcher.dispatch(message);
    verify(this.handler, Mockito.times(1)).handle(argument.capture());
  }
}
