package org.example.actor.testing.async.echo;

import akka.actor.testkit.typed.javadsl.LogCapturing;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class EchoTestLogCapturing {

    @ClassRule
    public static final TestKitJunitResource teskKit = new TestKitJunitResource();

    @Rule
    public final LogCapturing logCapturing = new LogCapturing();

    @Test
    public void ping() {
        ActorRef<Ping> pinger = teskKit.spawn(Echo.create(), "ping");
        TestProbe<Pong> probe = teskKit.createTestProbe();
        pinger.tell(Ping.of("hello", probe.getRef()));
        probe.expectMessage(Pong.of("hello"));
    }

}
