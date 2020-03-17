package org.example.actor.patterns.scheduling_messages_to_self;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<Batch> target = context.spawn(Target.create(), "target");
            ActorRef<Command> buncher = context.spawn(Buncher.create(target, Duration.ofSeconds(5), 10), "buncher");

            for (int i = 0; i < 100; i++) {
                buncher.tell(Number.of(i));
            }

            return Behaviors.receive(Void.class).onSignal(Terminated.class, sig -> Behaviors.stopped()).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "SchedulingMessageToSelfSystem");
    }

}
