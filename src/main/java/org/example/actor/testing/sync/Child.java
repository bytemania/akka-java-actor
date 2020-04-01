package org.example.actor.testing.sync;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Child {

    public static Behavior<String> create() {
        return Behaviors.receive((context, message) -> Behaviors.same());
    }

}
