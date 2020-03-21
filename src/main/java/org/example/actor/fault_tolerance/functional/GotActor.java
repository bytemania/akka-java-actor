package org.example.actor.fault_tolerance.functional;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GotActor {

    static Behavior<Got> create() {
        return Behaviors.receive(Got.class)
                .onMessage(Got.class, GotActor::onGet)
                .build();
    }

    private static Behavior<Got> onGet(Got got) {
        System.out.println("Got: " + got);
        return Behaviors.same();
    }

}
