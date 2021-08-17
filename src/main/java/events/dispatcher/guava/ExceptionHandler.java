package events.dispatcher.guava;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class ExceptionHandler implements SubscriberExceptionHandler {

  @Override
  public void handleException(Throwable throwable,
      SubscriberExceptionContext subscriberExceptionContext) {
    GuavaMessage event = (GuavaMessage) subscriberExceptionContext.getEvent();
    event.setException(throwable);
  }
}
