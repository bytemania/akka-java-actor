package org.example.actor.mailbox;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Main {

    private static Behavior<String> printer() {
        return Behaviors.setup(context -> Behaviors.receive(String.class)
                .onMessage(String.class, str -> {
                    context.getLog().info("Received {}", str);
                    return Behaviors.same();
                }).build());
    }

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<String> actorBounded = context.spawn(Main.printer(), "printerBounded",
                    MailboxSelector.bounded(100));
            actorBounded.tell("HERE");

            ActorRef<String> actorCustomMailBox = context.spawn(Main.printer(), "printerCustomMailBox",
                    MailboxSelector.fromConfig("my-custom-mailbox"));
            actorCustomMailBox.tell("HERE AGAIN");

            ActorRef<String> actorCustomMyMessageQueue = context.spawn(Main.printer(), "printerCustomMyMessageQueue",
                    MailboxSelector.fromConfig("custom-dispatcher-mailbox"));
            actorCustomMyMessageQueue.tell("HERE WE GO AGAIN");

            return Behaviors.receive(Void.class).onSignal(Terminated.class, sig -> Behaviors.stopped()).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "System");
    }

}
