package org.example.actor.fault_tolerance.signal;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<String> signals = context.spawn(Signal.create(), "signal");
            signals.tell("one");
            signals.tell("FAIL");

            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, sig -> Behaviors.stopped())
                    .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "SignalsSystem");
    }

}
