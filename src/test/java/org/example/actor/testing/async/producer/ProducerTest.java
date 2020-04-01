package org.example.actor.testing.async.producer;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ProducerTest {

    private static ActorTestKit testKit;
    private static TestProbe<Message> probe;

    @BeforeClass
    public static void setup() {
        testKit = ActorTestKit.create();
        probe = testKit.createTestProbe();
    }

    @AfterClass
    public static void teardown() {
        probe.stop();
        testKit.shutdownTestKit();
    }

    @Test
    public void producer() {
        Behavior<Message> mockedBehavior = Behaviors.receiveMessage(message -> {
            message.getReplyTo().tell(message.getI());
            return Behaviors.same();
        });

        ActorRef<Message> mockedPublisher = testKit.spawn(Behaviors.monitor(Message.class, probe.getRef(), mockedBehavior));
        Producer producer = new Producer(testKit.scheduler(), mockedPublisher);
        int messages = 3;
        producer.produce(messages);

        IntStream.range(0, messages).forEach(i -> {
            Message msg = probe.expectMessageClass(Message.class);
            assertEquals(i, msg.getI());
        });
    }

}
