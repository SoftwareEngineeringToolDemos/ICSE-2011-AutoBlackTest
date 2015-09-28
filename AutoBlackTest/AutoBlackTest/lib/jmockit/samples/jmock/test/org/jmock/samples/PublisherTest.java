package org.jmock.samples;

import org.jmock.*;
import org.jmock.integration.junit4.*;
import org.junit.*;
import org.junit.runner.*;

@RunWith(JMock.class)
public class PublisherTest
{
   Mockery context = new JUnit4Mockery();

   @Test
   public void oneSubscriberReceivesAMessage()
   {
      // set up
      final Subscriber subscriber = context.mock(Subscriber.class);

      Publisher publisher = new Publisher();
      publisher.add(subscriber);

      final String message = "message";

      // expectations
      context.checking(new Expectations()
      {
         {
            oneOf(subscriber).receive(message);
         }
      });

      // execute
      publisher.publish(message);
   }
}
