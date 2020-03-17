package org.example.actor.patterns.scheduling_messages_to_self;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Target {
    public static Behavior<Batch> create() {
        return Behaviors.setup(context -> Behaviors.receive(Batch.class)
                .onMessage(Batch.class, batch -> {
                    batch.getMessages().forEach(c -> System.out.println("Command received in batch: " + c));
                    return Behaviors.same();
                })
                .build());
    }
}
