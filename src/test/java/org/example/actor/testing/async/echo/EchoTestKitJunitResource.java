package org.example.actor.testing.async.echo;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Test;

public class EchoTestKitJunitResource {

    @ClassRule public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Test
    public void ping() {
        ActorRef<Ping> pinger = testKit.spawn(Echo.create(), "ping");
        TestProbe<Pong> probe = testKit.createTestProbe();
        pinger.tell(Ping.of("hello", probe.getRef()));
        probe.expectMessage(Pong.of("hello"));
    }
}
