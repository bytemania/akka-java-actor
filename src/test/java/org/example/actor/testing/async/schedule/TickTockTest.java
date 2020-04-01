package org.example.actor.testing.async.schedule;

import akka.actor.testkit.typed.javadsl.ManualTime;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import org.junit.ClassRule;
import org.junit.Test;

import java.time.Duration;

public class TickTockTest {

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource(ManualTime.config());

    @Test
    public void scheduledNonRepeatedTicks() {
        final ManualTime manualTime = ManualTime.get(testKit.system());
        TestProbe<Tock> probe = testKit.createTestProbe();
        testKit.spawn(TickTock.create(Duration.ofMillis(10), probe.getRef()));
        manualTime.expectNoMessageFor(Duration.ofMillis(9), probe);
        manualTime.timePasses(Duration.ofMillis(2));
        probe.expectMessage(Tock.INSTANCE);
        manualTime.expectNoMessageFor(Duration.ofSeconds(10), probe);
    }

}
