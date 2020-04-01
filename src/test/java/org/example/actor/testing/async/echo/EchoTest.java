package org.example.actor.testing.async.echo;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

public class EchoTest {

    private static ActorTestKit testKit;
    private static TestProbe<Pong> probe;

    @BeforeClass
    public static void setup() {
        testKit = ActorTestKit.create();
        probe = testKit.createTestProbe();
    }

    @AfterClass
    public static void cleanup() {
        probe.stop();
        testKit.shutdownTestKit();
    }

    @Test
    public void ping() {
        ActorRef<Ping> pinger = testKit.spawn(Echo.create(), "ping");
        pinger.tell(Ping.of("hello", probe.getRef()));
        probe.expectMessage(Pong.of("hello"));
    }

    @Test
    public void stop() {
        ActorRef<Ping> pinger1 = testKit.spawn(Echo.create(), "pinger");
        pinger1.tell(Ping.of("hello", probe.getRef()));
        probe.expectMessage(Pong.of("hello"));
        testKit.stop(pinger1);
        probe.expectTerminated(pinger1);

        ActorRef<Ping> pinger2 = testKit.spawn(Echo.create(), "pinger");
        pinger2.tell(Ping.of("hello", probe.getRef()));
        probe.expectMessage(Pong.of("hello"));
        testKit.stop(pinger2, Duration.ofSeconds(10));
        probe.expectTerminated(pinger2);
    }

}
