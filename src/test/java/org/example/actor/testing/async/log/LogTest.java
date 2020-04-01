package org.example.actor.testing.async.log;

import akka.actor.testkit.typed.javadsl.LoggingTestKit;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.event.Level;

public class LogTest {

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Test
    public void log() {
        ActorRef<Message> ref = testKit.spawn(Log.createConstantLog());
        LoggingTestKit.info("Message received").expect(testKit.system(), () -> {
            ref.tell(Message.of("hello"));
            return null;
        });
    }

    @Test
    public void customLog() {
        ActorRef<Message> ref = testKit.spawn(Log.createError());
        LoggingTestKit.error(IllegalArgumentException.class)
                .withOccurrences(1)
                .withMessageRegex("Error received")
                .withLogLevel(Level.ERROR)
                .expect(testKit.system(), () -> {
                    ref.tell(Message.of("hello"));
                    return null;
                });

    }

}
