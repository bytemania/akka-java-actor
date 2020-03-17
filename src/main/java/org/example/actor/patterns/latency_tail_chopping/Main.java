package org.example.actor.patterns.latency_tail_chopping;

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
                ActorRef<RequestPrice> hotel = context.spawn(Hotel.create(), "hotel");
                context.spawn(PriceRequester.create(hotel), "priceRequester");
                return Behaviors.receive(Void.class).onSignal(Terminated.class, sig -> Behaviors.stopped()).build();
            });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "LatencyTailChopping");
    }

}
