package org.example.actor.testing.sync;

import akka.actor.testkit.typed.CapturedLogEvent;
import akka.actor.testkit.typed.Effect;
import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import org.junit.Test;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HelloTest {

    @Test
    public void spawningChild() {
        BehaviorTestKit<Command> test = BehaviorTestKit.create(Hello.create());
        test.run(CreateAChild.of("child"));
        assertEquals("child", test.expectEffectClass(Effect.Spawned.class).childName());
    }

    @Test
    public void spawingAnonymousChild() {
        BehaviorTestKit<Command> test = BehaviorTestKit.create(Hello.create());
        test.run(CreateAnAnonymousChild.INSTANCE);
        test.expectEffectClass(Effect.SpawnedAnonymous.class);
    }

    @Test
    public void send() {
        BehaviorTestKit<Command> test = BehaviorTestKit.create(Hello.create());
        TestInbox<String> inbox = TestInbox.create();
        test.run(SayHello.of(inbox.getRef()));
        inbox.expectMessage("hello");
    }

    @Test
    public void sendToNamedActor() {
        BehaviorTestKit<Command> testKit = BehaviorTestKit.create(Hello.create());
        testKit.run(SayHelloToChild.of("child"));
        TestInbox<String> childInbox = testKit.childInbox("child");
        childInbox.expectMessage("hello");
    }

    @Test
    public void sendToAnonymousActor() {
        BehaviorTestKit<Command> testKit = BehaviorTestKit.create(Hello.create());
        testKit.run(SayHelloToAnonymousChild.INSTANCE);
        //Anonymous actors are created as: $a $b etc
        TestInbox<String> childInbox = testKit.childInbox("$a");
        childInbox.expectMessage("hello stranger");
    }

    @Test
    public void checkLog() {
         BehaviorTestKit<Command> test = BehaviorTestKit.create(Hello.create());
         TestInbox<String> inbox = TestInbox.create("Inboxer");
         test.run(LogAndSayHello.of(inbox.getRef()));

         List<CapturedLogEvent> allLogEntries = test.getAllLogEntries();
         assertEquals(1, allLogEntries.size());
         CapturedLogEvent expectedLogEvent = new CapturedLogEvent(
                 Level.INFO,
                 "Saying hello to Inboxer",
                 Optional.empty(),
                 Optional.empty(),
                 new HashMap<>());
         assertEquals(expectedLogEvent, allLogEntries.get(0));
    }

}
