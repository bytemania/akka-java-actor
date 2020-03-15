package org.example.actor.patterns.request_response_ask;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Main {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
                    ActorRef<HalCommand> hal = context.spawn(Hal.create(), "hal");
                    context.spawn(Dave.create(hal), "dave");
                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "MainRequestResponseAsk");
    }

}
