package org.example.actor.fault_tolerance.signal;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.PreRestart;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Signal {

    static Behavior<String> create() {
        return Behaviors.<String>supervise(
                Behaviors.setup(ctx ->
                        Behaviors.receive(String.class)
                                .onMessage(String.class, msg -> {
                                    if ("FAIL".equals(msg)) throw new IllegalArgumentException("FAIL");
                                    System.out.println("Processing msg " + msg);
                                    return Behaviors.same();
                                })
                                .onSignal(PreRestart.class, signal -> {
                                    System.out.println("Get the state");
                                    return Behaviors.same();
                                })
                                .onSignal(PostStop.class, signal -> {
                                    System.out.println("Save the state");
                                    return Behaviors.same();
                                })
                                .build()
        )).onFailure(Exception.class, SupervisorStrategy.restart());
    }

}
