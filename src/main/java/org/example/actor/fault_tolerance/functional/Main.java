package org.example.actor.fault_tolerance.functional;

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
            ActorRef<Got> got = context.spawn(GotActor.create(), "got");
            ActorRef<Command> counter = context.spawn(Counter.create(), "counter");
            counter.tell(Get.of(got.narrow()));
            counter.tell(Error.of());
            counter.tell(Get.of(got.narrow()));
            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, signal -> Behaviors.stopped())
                    .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "FaultToleranceSystem");
    }

}
